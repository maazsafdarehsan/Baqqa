package org.Baqqaproject.Baqqa.attachment;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.data.BdfEntry;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.NoSuchMessageException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.db.TransactionManager;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.attachment.Attachment;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_ATTACHMENT_CHUNK_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_TOTAL_ATTACHMENT_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_TOTAL_CHUNKS;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_ATTACHMENT_ID;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_CHUNK_INDEX;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_CONTENT_TYPE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_DESCRIPTOR_LENGTH;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_TOTAL_CHUNKS;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_TOTAL_SIZE;

public class AttachmentReaderImpl implements AttachmentReader {

	// Metadata key and message-type value are duplicated from (package-private)
	// MessagingConstants/MessageTypes in the messaging package.
	private static final String MSG_KEY_MSG_TYPE = "messageType";
	private static final int MSG_TYPE_ATTACHMENT = 1;

	private final TransactionManager db;
	private final ClientHelper clientHelper;

	@Inject
	public AttachmentReaderImpl(TransactionManager db,
			ClientHelper clientHelper) {
		this.db = db;
		this.clientHelper = clientHelper;
	}

	@Override
	public Attachment getAttachment(AttachmentHeader h) throws DbException {
		return db.transactionWithResult(true, txn -> getAttachment(txn, h));
	}

	@Override
	public Attachment getAttachment(Transaction txn, AttachmentHeader h)
			throws DbException {
		MessageId m = h.getMessageId();
		Message message = clientHelper.getMessage(txn, m);
		// Check that the message is in the expected group, to prevent it from
		// being loaded in the context of a different group
		if (!message.getGroupId().equals(h.getGroupId())) {
			throw new NoSuchMessageException();
		}
		byte[] body = message.getBody();
		try {
			BdfDictionary meta =
					clientHelper.getMessageMetadataAsDictionary(txn, m);
			String contentType = meta.getString(MSG_KEY_CONTENT_TYPE);
			if (!contentType.equals(h.getContentType()))
				throw new NoSuchMessageException();
			int offset = meta.getInt(MSG_KEY_DESCRIPTOR_LENGTH);
			if (offset < 0 || offset > body.length)
				throw new NoSuchMessageException();
			byte[] attachmentId = meta.getOptionalRaw(MSG_KEY_ATTACHMENT_ID);
			if (attachmentId == null) {
				// Single-message attachment
				InputStream stream = new ByteArrayInputStream(body, offset,
						body.length - offset);
				return new Attachment(h, stream);
			}
			// Chunked attachment: reassemble from this chunk and its siblings
			return getChunkedAttachment(txn, h, meta, attachmentId);
		} catch (FormatException e) {
			throw new NoSuchMessageException();
		}
	}

	/**
	 * Reassembles a chunked attachment from its physical chunk messages.
	 * The chunks may have arrived in any order. Throws
	 * {@link NoSuchMessageException} if not all chunks are present, if a chunk
	 * index is duplicated or out of range, or if the reassembled data does not
	 * match the declared total size.
	 */
	private Attachment getChunkedAttachment(Transaction txn, AttachmentHeader h,
			BdfDictionary meta0, byte[] attachmentId)
			throws DbException, FormatException {
		long totalChunks = meta0.getLong(MSG_KEY_TOTAL_CHUNKS);
		long totalSize = meta0.getLong(MSG_KEY_TOTAL_SIZE);
		if (totalChunks < 1 || totalChunks > MAX_TOTAL_CHUNKS)
			throw new NoSuchMessageException();
		if (totalSize <= 0 || totalSize > MAX_TOTAL_ATTACHMENT_SIZE)
			throw new NoSuchMessageException();
		// Find all chunk messages belonging to this logical attachment
		BdfDictionary query = BdfDictionary.of(
				new BdfEntry(MSG_KEY_MSG_TYPE, MSG_TYPE_ATTACHMENT),
				new BdfEntry(MSG_KEY_ATTACHMENT_ID, attachmentId));
		Map<MessageId, BdfDictionary> chunkMeta = clientHelper
				.getMessageMetadataAsDictionary(txn, h.getGroupId(), query);
		// Detect duplicate chunk indices while collecting chunk ids by index
		MessageId[] chunkMap = new MessageId[(int) totalChunks];
		for (Entry<MessageId, BdfDictionary> e : chunkMeta.entrySet()) {
			BdfDictionary meta = e.getValue();
			long ci = meta.getLong(MSG_KEY_CHUNK_INDEX);
			if (ci < 0 || ci >= totalChunks) throw new NoSuchMessageException();
			int idx = (int) ci;
			if (chunkMap[idx] != null) throw new NoSuchMessageException();
			chunkMap[idx] = e.getKey();
		}
		// Verify that all chunks are present
		for (int i = 0; i < totalChunks; i++) {
			if (chunkMap[i] == null) throw new NoSuchMessageException();
		}
		// Reassemble the chunks into a temporary file, streaming each chunk
		// body without ever keeping the whole attachment in memory.
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile("baqqa-attachment", ".bin");
			long sum = 0;
			try (OutputStream out = new FileOutputStream(tmpFile)) {
				for (int i = 0; i < totalChunks; i++) {
					Message cm = clientHelper.getMessage(txn, chunkMap[i]);
					if (!cm.getGroupId().equals(h.getGroupId()))
						throw new NoSuchMessageException();
					BdfDictionary meta = chunkMeta.get(chunkMap[i]);
					int offset = meta.getInt(MSG_KEY_DESCRIPTOR_LENGTH);
					byte[] body = cm.getBody();
					if (offset < 0 || offset > body.length)
						throw new NoSuchMessageException();
					int len = body.length - offset;
					if (len < 1 || len > MAX_ATTACHMENT_CHUNK_SIZE)
						throw new NoSuchMessageException();
					out.write(body, offset, len);
					sum += len;
				}
			}
			if (sum != totalSize) throw new NoSuchMessageException();
			InputStream stream = new FileInputStream(tmpFile);
			return new Attachment(h,
					new DeleteOnCloseInputStream(stream, tmpFile), sum);
		} catch (DbException e) {
			if (tmpFile != null) deleteQuietly(tmpFile);
			throw e;
		} catch (IOException e) {
			if (tmpFile != null) deleteQuietly(tmpFile);
			throw new DbException(e);
		}
	}

	private static void deleteQuietly(File file) {
		if (!file.delete()) file.deleteOnExit();
	}

	/**
	 * An {@link InputStream} that deletes a temporary file when closed.
	 */
	private static class DeleteOnCloseInputStream extends FilterInputStream {
		private final File file;
		private boolean closed = false;

		DeleteOnCloseInputStream(InputStream in, File file) {
			super(in);
			this.file = file;
		}

		@Override
		public void close() throws IOException {
			if (closed) return;
			closed = true;
			super.close();
			deleteQuietly(file);
		}
	}

}



