package org.Baqqaproject.Baqqa.attachment;

import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.data.BdfEntry;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.db.NoSuchMessageException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.db.TransactionManager;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.bramble.test.BrambleMockTestCase;
import org.Baqqaproject.bramble.test.DbExpectations;
import org.Baqqaproject.Baqqa.api.attachment.Attachment;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static java.lang.System.arraycopy;
import static org.Baqqaproject.bramble.test.TestUtils.getMessage;
import static org.Baqqaproject.bramble.test.TestUtils.getRandomId;
import static org.Baqqaproject.bramble.util.IoUtils.copyAndClose;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_ATTACHMENT_CHUNK_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_ATTACHMENT_ID;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_CHUNK_INDEX;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_CONTENT_TYPE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_DESCRIPTOR_LENGTH;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_TOTAL_CHUNKS;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_TOTAL_SIZE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AttachmentReaderImplTest extends BrambleMockTestCase {

	private final TransactionManager db = context.mock(DatabaseComponent.class);
	private final ClientHelper clientHelper = context.mock(ClientHelper.class);

	private final GroupId groupId = new GroupId(getRandomId());
	private final Message message = getMessage(groupId, 1234);
	private final String contentType = "image/jpeg";
	private final AttachmentHeader header = new AttachmentHeader(groupId,
			message.getId(), contentType);

	private final AttachmentReaderImpl attachmentReader =
			new AttachmentReaderImpl(db, clientHelper);

	@Test(expected = NoSuchMessageException.class)
	public void testWrongGroup() throws Exception {
		GroupId wrongGroupId = new GroupId(getRandomId());
		AttachmentHeader wrongGroup = new AttachmentHeader(wrongGroupId,
				message.getId(), contentType);

		Transaction txn = new Transaction(null, true);

		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(true), withDbCallable(txn));
			oneOf(clientHelper).getMessage(txn, message.getId());
			will(returnValue(message));
		}});

		attachmentReader.getAttachment(wrongGroup);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testMissingContentType() throws Exception {
		BdfDictionary meta = new BdfDictionary();

		testInvalidMetadata(header, message, meta);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testWrongContentType() throws Exception {
		BdfDictionary meta = BdfDictionary.of(
				new BdfEntry(MSG_KEY_CONTENT_TYPE, "image/png"));

		testInvalidMetadata(header, message, meta);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testMissingDescriptorLength() throws Exception {
		BdfDictionary meta = BdfDictionary.of(
				new BdfEntry(MSG_KEY_CONTENT_TYPE, contentType));

		testInvalidMetadata(header, message, meta);
	}

	private void testInvalidMetadata(AttachmentHeader h, Message msg,
			BdfDictionary meta) throws Exception {
		Transaction txn = new Transaction(null, true);

		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(true), withDbCallable(txn));
			oneOf(clientHelper).getMessage(txn, msg.getId());
			will(returnValue(msg));
			oneOf(clientHelper)
					.getMessageMetadataAsDictionary(txn, msg.getId());
			will(returnValue(meta));
		}});

		attachmentReader.getAttachment(h);
	}

	@Test
	public void testSkipsDescriptor() throws Exception {
		int descriptorLength = 123;
		BdfDictionary meta = BdfDictionary.of(
				new BdfEntry(MSG_KEY_CONTENT_TYPE, contentType),
				new BdfEntry(MSG_KEY_DESCRIPTOR_LENGTH, descriptorLength));

		byte[] body = message.getBody();
		byte[] expectedData = new byte[body.length - descriptorLength];
		arraycopy(body, descriptorLength, expectedData, 0,
				expectedData.length);

		Transaction txn = new Transaction(null, true);

		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(true), withDbCallable(txn));
			oneOf(clientHelper).getMessage(txn, message.getId());
			will(returnValue(message));
			oneOf(clientHelper)
					.getMessageMetadataAsDictionary(txn, message.getId());
			will(returnValue(meta));
		}});

		Attachment attachment = attachmentReader.getAttachment(header);
		InputStream in = attachment.getStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyAndClose(in, out);
		byte[] data = out.toByteArray();

		assertArrayEquals(expectedData, data);
	}

	// Chunked (multi-message) attachments

	private final byte[] chunkAttachmentId = getRandomId();

	private BdfDictionary getChunkMeta(int descriptorLength, long chunkIndex,
			long totalChunks, long totalSize) {
		return BdfDictionary.of(
				new BdfEntry(MSG_KEY_CONTENT_TYPE, contentType),
				new BdfEntry(MSG_KEY_DESCRIPTOR_LENGTH, descriptorLength),
				new BdfEntry(MSG_KEY_ATTACHMENT_ID, chunkAttachmentId),
				new BdfEntry(MSG_KEY_CHUNK_INDEX, chunkIndex),
				new BdfEntry(MSG_KEY_TOTAL_CHUNKS, totalChunks),
				new BdfEntry(MSG_KEY_TOTAL_SIZE, totalSize));
	}

	/** Two chunks arriving in reverse order must reassemble correctly. */
	@Test
	public void testChunkedAttachmentIsReassembledInChunkOrder()
			throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		Message chunk1 = getMessage(groupId, 800);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		long totalSize = 1000 + 800;
		BdfDictionary meta0 = getChunkMeta(0, 0, 2, totalSize);
		BdfDictionary meta1 = getChunkMeta(0, 1, 2, totalSize);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk1.getId(), meta1); // Higher index comes first
		chunkMeta.put(chunk0.getId(), meta0);
		byte[] expected = new byte[(int) totalSize];
		arraycopy(chunk0.getBody(), 0, expected, 0, 1000);
		arraycopy(chunk1.getBody(), 0, expected, 1000, 800);

		Transaction txn = new Transaction(null, true);

		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(true), withDbCallable(txn));
			allowing(clientHelper).getMessage(txn, chunk0.getId());
			will(returnValue(chunk0));
			oneOf(clientHelper)
					.getMessageMetadataAsDictionary(txn, chunk0.getId());
			will(returnValue(meta0));
			oneOf(clientHelper).getMessageMetadataAsDictionary(with(any(Transaction.class)), with(any(GroupId.class)),
					with(any(BdfDictionary.class)));
			will(returnValue(chunkMeta));
			// Chunks are re-fetched in index order
			allowing(clientHelper).getMessage(txn, chunk0.getId());
			will(returnValue(chunk0));
			allowing(clientHelper).getMessage(txn, chunk1.getId());
			will(returnValue(chunk1));
		}});

		Attachment attachment = attachmentReader.getAttachment(h);
		assertEquals(totalSize, attachment.getTotalSize());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyAndClose(attachment.getStream(), out);

		assertArrayEquals(expected, out.toByteArray());
	}

	@Test(expected = NoSuchMessageException.class)
	public void testMissingChunkIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		BdfDictionary meta0 = getChunkMeta(0, 0, 2, 1000);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, null);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testDuplicateChunkIndexIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		Message chunk1 = getMessage(groupId, 800);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		long totalSize = 1800;
		// Both chunks claim index 0
		BdfDictionary meta0 = getChunkMeta(0, 0, 2, totalSize);
		BdfDictionary meta1 = getChunkMeta(0, 0, 2, totalSize);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);
		chunkMeta.put(chunk1.getId(), meta1);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, null);
	}


	@Test(expected = NoSuchMessageException.class)
	public void testOutOfRangeChunkIndexIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		Message chunk1 = getMessage(groupId, 800);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		long totalSize = 1800;
		BdfDictionary meta0 = getChunkMeta(0, 0, 2, totalSize);
		// Index 2 is out of range for 2 chunks
		BdfDictionary meta1 = getChunkMeta(0, 2, 2, totalSize);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);
		chunkMeta.put(chunk1.getId(), meta1);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, null);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testInvalidTotalChunksIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		// Total chunks must be positive
		BdfDictionary meta0 = getChunkMeta(0, 0, 0, 1000);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, null);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testTotalSizeMismatchIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		Message chunk1 = getMessage(groupId, 800);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		// Declared total size does not match the reassembled data
		BdfDictionary meta0 = getChunkMeta(0, 0, 2, 1799);
		BdfDictionary meta1 = getChunkMeta(0, 1, 2, 1799);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);
		chunkMeta.put(chunk1.getId(), meta1);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, chunk1);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testCorruptedChunkMetadataIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		// Missing the chunk index
		BdfDictionary meta0 = BdfDictionary.of(
				new BdfEntry(MSG_KEY_CONTENT_TYPE, contentType),
				new BdfEntry(MSG_KEY_DESCRIPTOR_LENGTH, 0),
				new BdfEntry(MSG_KEY_ATTACHMENT_ID, chunkAttachmentId),
				new BdfEntry(MSG_KEY_TOTAL_CHUNKS, 2L),
				new BdfEntry(MSG_KEY_TOTAL_SIZE, 1000L));
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, null);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testOversizedChunkDataIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		Message chunk1 = getMessage(groupId, 32000);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		long totalSize = 1000 + 32000;
		BdfDictionary meta0 = getChunkMeta(0, 0, 2, totalSize);
		// Chunk 1's data exceeds MAX_ATTACHMENT_CHUNK_SIZE
		BdfDictionary meta1 = getChunkMeta(0, 1, 2, totalSize);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);
		chunkMeta.put(chunk1.getId(), meta1);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, chunk1);
	}

	@Test(expected = NoSuchMessageException.class)
	public void testEmptyChunkDataIsRejected() throws Exception {
		Message chunk0 = getMessage(groupId, 1000);
		Message chunk1 = getMessage(groupId, 800);
		AttachmentHeader h =
				new AttachmentHeader(groupId, chunk0.getId(), contentType);
		long totalSize = 1800;
		BdfDictionary meta0 = getChunkMeta(0, 0, 2, totalSize);
		// The descriptor covers the whole body, leaving no data
		BdfDictionary meta1 = getChunkMeta(800, 1, 2, totalSize);
		Map<MessageId, BdfDictionary> chunkMeta = new LinkedHashMap<>();
		chunkMeta.put(chunk0.getId(), meta0);
		chunkMeta.put(chunk1.getId(), meta1);

		testInvalidChunkMetadata(h, chunk0, meta0, chunkMeta, chunk1);
	}

	private void testInvalidChunkMetadata(AttachmentHeader h, Message chunk0,
			BdfDictionary meta0, Map<MessageId, BdfDictionary> chunkMeta,
			@Nullable Message extraChunk) throws Exception {
		Transaction txn = new Transaction(null, true);

		context.checking(new DbExpectations() {{
			oneOf(db).transactionWithResult(with(true), withDbCallable(txn));
			allowing(clientHelper).getMessage(txn, chunk0.getId());
			will(returnValue(chunk0));
			if (extraChunk != null) {
				allowing(clientHelper)
						.getMessage(txn, extraChunk.getId());
				will(returnValue(extraChunk));
			}
			oneOf(clientHelper)
					.getMessageMetadataAsDictionary(txn, chunk0.getId());
			will(returnValue(meta0));
			// The reader may reject before or after querying siblings
			allowing(clientHelper).getMessageMetadataAsDictionary(
					with(any(Transaction.class)), with(any(GroupId.class)),
					with(any(BdfDictionary.class)));
			will(returnValue(chunkMeta));
		}});

		attachmentReader.getAttachment(h);
	}
}

