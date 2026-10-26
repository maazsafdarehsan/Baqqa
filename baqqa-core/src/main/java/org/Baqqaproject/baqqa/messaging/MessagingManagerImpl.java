package org.Baqqaproject.Baqqa.messaging;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.UniqueId;
import org.Baqqaproject.bramble.api.cleanup.CleanupHook;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.client.ContactGroupFactory;
import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.contact.ContactManager.ContactHook;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.data.BdfEntry;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.bramble.api.data.MetadataParser;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Metadata;
import org.Baqqaproject.bramble.api.db.NoSuchMessageException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager.OpenDatabaseHook;
import org.Baqqaproject.bramble.api.sync.Group;
import org.Baqqaproject.bramble.api.sync.Group.Visibility;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.InvalidMessageException;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.bramble.api.sync.MessageStatus;
import org.Baqqaproject.bramble.api.sync.validation.IncomingMessageHook;
import org.Baqqaproject.bramble.api.versioning.ClientVersioningManager;
import org.Baqqaproject.bramble.api.versioning.ClientVersioningManager.ClientVersioningHook;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.attachment.FileTooBigException;
import org.Baqqaproject.Baqqa.api.autodelete.AutoDeleteManager;
import org.Baqqaproject.Baqqa.api.autodelete.event.ConversationMessagesDeletedEvent;
import org.Baqqaproject.Baqqa.api.client.MessageTracker;
import org.Baqqaproject.Baqqa.api.client.MessageTracker.GroupCount;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager.ConversationClient;
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageHeader;
import org.Baqqaproject.Baqqa.api.conversation.DeletionResult;
import org.Baqqaproject.Baqqa.api.messaging.MessagingManager;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessage;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFormat;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageHeader;
import org.Baqqaproject.Baqqa.api.messaging.event.AttachmentReceivedEvent;
import org.Baqqaproject.Baqqa.api.messaging.event.PrivateMessageReceivedEvent;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static java.util.Collections.emptyList;
import static java.util.logging.Logger.getLogger;
import static org.Baqqaproject.bramble.api.client.ContactGroupConstants.GROUP_KEY_CONTACT_ID;
import static org.Baqqaproject.bramble.api.sync.SyncConstants.MAX_MESSAGE_BODY_LENGTH;
import static org.Baqqaproject.bramble.api.sync.validation.IncomingMessageHook.DeliveryAction.ACCEPT_DO_NOT_SHARE;
import static org.Baqqaproject.bramble.util.LogUtils.logDuration;
import static org.Baqqaproject.bramble.util.LogUtils.now;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_ATTACHMENT_CHUNK_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_SINGLE_ATTACHMENT_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_TOTAL_ATTACHMENT_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_TOTAL_CHUNKS;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_ATTACHMENT_ID;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_CHUNK_INDEX;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_CONTENT_TYPE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_DESCRIPTOR_LENGTH;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_FILE_NAME;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_TOTAL_CHUNKS;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_TOTAL_SIZE;
import static org.Baqqaproject.Baqqa.api.autodelete.AutoDeleteConstants.NO_AUTO_DELETE_TIMER;
import static org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFormat.TEXT_IMAGES;
import static org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFormat.TEXT_IMAGES_AUTO_DELETE;
import static org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFormat.TEXT_ONLY;
import static org.Baqqaproject.Baqqa.client.MessageTrackerConstants.MSG_KEY_READ;
import static org.Baqqaproject.Baqqa.messaging.MessageTypes.ATTACHMENT;
import static org.Baqqaproject.Baqqa.messaging.MessageTypes.PRIVATE_MESSAGE;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MISSING_ATTACHMENT_CLEANUP_DURATION_MS;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MSG_KEY_ATTACHMENT_HEADERS;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MSG_KEY_AUTO_DELETE_TIMER;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MSG_KEY_HAS_TEXT;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MSG_KEY_LOCAL;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MSG_KEY_MSG_TYPE;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MSG_KEY_TIMESTAMP;

@Immutable
@NotNullByDefault
class MessagingManagerImpl implements MessagingManager, IncomingMessageHook,
		ConversationClient, OpenDatabaseHook, ContactHook,
		ClientVersioningHook, CleanupHook {

	private static final Logger LOG =
			getLogger(MessagingManagerImpl.class.getName());

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private final DatabaseComponent db;
	private final ClientHelper clientHelper;
	private final MetadataParser metadataParser;
	private final ConversationManager conversationManager;
	private final MessageTracker messageTracker;
	private final ClientVersioningManager clientVersioningManager;
	private final ContactGroupFactory contactGroupFactory;
	private final AutoDeleteManager autoDeleteManager;

	@Inject
	MessagingManagerImpl(
			DatabaseComponent db,
			ClientHelper clientHelper,
			ClientVersioningManager clientVersioningManager,
			MetadataParser metadataParser,
			ConversationManager conversationManager,
			MessageTracker messageTracker,
			ContactGroupFactory contactGroupFactory,
			AutoDeleteManager autoDeleteManager) {
		this.db = db;
		this.clientHelper = clientHelper;
		this.metadataParser = metadataParser;
		this.conversationManager = conversationManager;
		this.messageTracker = messageTracker;
		this.clientVersioningManager = clientVersioningManager;
		this.contactGroupFactory = contactGroupFactory;
		this.autoDeleteManager = autoDeleteManager;
	}

	@Override
	public GroupCount getGroupCount(Transaction txn, ContactId contactId)
			throws DbException {
		Contact contact = db.getContact(txn, contactId);
		GroupId groupId = getContactGroup(contact).getId();
		return messageTracker.getGroupCount(txn, groupId);
	}

	@Override
	public void onDatabaseOpened(Transaction txn) throws DbException {
		// Create a local group to indicate that we've set this client up
		Group localGroup = contactGroupFactory.createLocalGroup(CLIENT_ID,
				MAJOR_VERSION);
		if (db.containsGroup(txn, localGroup.getId())) return;
		db.addGroup(txn, localGroup);
		// Set things up for any pre-existing contacts
		for (Contact c : db.getContacts(txn)) addingContact(txn, c);
	}

	@Override
	public void addingContact(Transaction txn, Contact c) throws DbException {
		// Create a group to share with the contact
		Group g = getContactGroup(c);
		db.addGroup(txn, g);
		// Apply the client's visibility to the contact group
		Visibility client = clientVersioningManager.getClientVisibility(txn,
				c.getId(), CLIENT_ID, MAJOR_VERSION);
		db.setGroupVisibility(txn, c.getId(), g.getId(), client);
		// Attach the contact ID to the group
		clientHelper.setContactId(txn, g.getId(), c.getId());
		// Initialize the group count with current time
		messageTracker.initializeGroupCount(txn, g.getId());
	}

	@Override
	public Group getContactGroup(Contact c) {
		return contactGroupFactory.createContactGroup(CLIENT_ID,
				MAJOR_VERSION, c);
	}

	@Override
	public void removingContact(Transaction txn, Contact c) throws DbException {
		db.removeGroup(txn, getContactGroup(c));
	}

	@Override
	public void onClientVisibilityChanging(Transaction txn, Contact c,
			Visibility v) throws DbException {
		// Apply the client's visibility to the contact group
		Group g = getContactGroup(c);
		db.setGroupVisibility(txn, c.getId(), g.getId(), v);
	}

	@Override
	public DeliveryAction incomingMessage(Transaction txn, Message m,
			Metadata meta) throws DbException, InvalidMessageException {
		try {
			BdfDictionary metaDict = metadataParser.parse(meta);
			// Message type is null for version 0.0 private messages
			Integer messageType = metaDict.getOptionalInt(MSG_KEY_MSG_TYPE);
			if (messageType == null) {
				incomingPrivateMessage(txn, m, metaDict, true, emptyList());
			} else if (messageType == PRIVATE_MESSAGE) {
				boolean hasText = metaDict.getBoolean(MSG_KEY_HAS_TEXT);
				List<AttachmentHeader> headers =
						parseAttachmentHeaders(txn, m.getGroupId(), metaDict);
				incomingPrivateMessage(txn, m, metaDict, hasText, headers);
			} else if (messageType == ATTACHMENT) {
				incomingAttachment(txn, m);
			} else {
				throw new InvalidMessageException();
			}
		} catch (FormatException e) {
			throw new InvalidMessageException(e);
		}
		return ACCEPT_DO_NOT_SHARE;
	}

	private void incomingPrivateMessage(Transaction txn, Message m,
			BdfDictionary meta, boolean hasText, List<AttachmentHeader> headers)
			throws DbException, FormatException {
		long start = now();
		GroupId groupId = m.getGroupId();
		long timestamp = meta.getLong(MSG_KEY_TIMESTAMP);
		boolean local = meta.getBoolean(MSG_KEY_LOCAL);
		boolean read = meta.getBoolean(MSG_KEY_READ);
		long timer = meta.getLong(MSG_KEY_AUTO_DELETE_TIMER,
				NO_AUTO_DELETE_TIMER);
		PrivateMessageHeader header =
				new PrivateMessageHeader(m.getId(), groupId, timestamp, local,
						read, false, false, hasText, headers, timer);
		ContactId contactId = getContactId(txn, groupId);
		PrivateMessageReceivedEvent event =
				new PrivateMessageReceivedEvent(header, contactId);
		txn.attach(event);
		conversationManager.trackIncomingMessage(txn, m);
		if (timer != NO_AUTO_DELETE_TIMER) {
			db.setCleanupTimerDuration(txn, m.getId(), timer);
		}
		autoDeleteManager.receiveAutoDeleteTimer(txn, contactId, timer,
				timestamp);
		if (!headers.isEmpty()) stopAttachmentCleanupTimers(txn, m, headers);
		logDuration(LOG, "Receiving private message", start);
	}

		private List<AttachmentHeader> parseAttachmentHeaders(Transaction txn,
			GroupId g, BdfDictionary meta) throws FormatException, DbException {
		BdfList attachmentHeaders = meta.getList(MSG_KEY_ATTACHMENT_HEADERS);
		int length = attachmentHeaders.size();
		List<AttachmentHeader> headers = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			BdfList header = attachmentHeaders.getList(i);
			MessageId m = new MessageId(header.getRaw(0));
			String contentType = header.getString(1);
			// Look up the chunk metadata for the display name and size
			// (only present for generic files; missing for legacy images).
			// For legacy single-message attachments, this lookup is cheap and
			// simply returns the message's own metadata. For chunked attachments,
			// it returns the first chunk message's metadata which carries the
			// same logical attachment ID, total size, and file name.
			String fileName = null;
			long totalSize = -1;
			try {
				BdfDictionary chunkMeta =
						clientHelper.getMessageMetadataAsDictionary(txn, m);
				String name = chunkMeta.getOptionalString(MSG_KEY_FILE_NAME);
				if (name != null) fileName = name;
				Long size = chunkMeta.getOptionalLong(MSG_KEY_TOTAL_SIZE);
				if (size != null) totalSize = size;
			} catch (NoSuchMessageException e) {
				// The attachment (or its first chunk) has not been received,
				// or has already been deleted or cleaned up. Keep the default
				// file name and size so the private message can still be
				// delivered and shown in the conversation.
			}
			headers.add(new AttachmentHeader(g, m, contentType, fileName,
					totalSize));
		}
		return headers;
	}

	private void stopAttachmentCleanupTimers(Transaction txn, Message m,
			List<AttachmentHeader> headers)
			throws DbException, FormatException {
		// Fetch the IDs of all remote attachments that have been delivered
		BdfDictionary query = BdfDictionary.of(
				new BdfEntry(MSG_KEY_MSG_TYPE, ATTACHMENT),
				new BdfEntry(MSG_KEY_LOCAL, false));
		Collection<MessageId> results =
				clientHelper.getMessageIds(txn, m.getGroupId(), query);
		// Stop the cleanup timers of any attachments (and their chunks) that
		// have already been delivered
		for (AttachmentHeader h : headers) {
			if (results.contains(h.getMessageId())) {
				for (MessageId id :
						getAttachmentMessageIds(txn, m.getGroupId(), h)) {
					if (results.contains(id)) db.stopCleanupTimer(txn, id);
				}
			}
		}
	}

	private void incomingAttachment(Transaction txn, Message m)
			throws DbException {
		long start = now();
		ContactId contactId = getContactId(txn, m.getGroupId());
		txn.attach(new AttachmentReceivedEvent(m.getId(), contactId));
		// If no private messages that list this attachment have been
		// delivered, start the cleanup timer. It will be stopped when a
		// private message that lists this attachment is delivered
		BdfDictionary query = BdfDictionary.of(
				new BdfEntry(MSG_KEY_MSG_TYPE, PRIVATE_MESSAGE),
				new BdfEntry(MSG_KEY_LOCAL, false));
		try {
			Map<MessageId, BdfDictionary> results = clientHelper
					.getMessageMetadataAsDictionary(txn, m.getGroupId(), query);
			// IDs of all delivered attachment messages, so that we only try
			// to resolve headers whose first chunk has already arrived
			Collection<MessageId> deliveredAttachments = clientHelper
					.getMessageIds(txn, m.getGroupId(), BdfDictionary.of(
							new BdfEntry(MSG_KEY_MSG_TYPE, ATTACHMENT),
							new BdfEntry(MSG_KEY_LOCAL, false)));
			for (BdfDictionary meta : results.values()) {
				List<AttachmentHeader> headers =
						parseAttachmentHeaders(txn, m.getGroupId(), meta);
				for (AttachmentHeader h : headers) {
					if (!deliveredAttachments.contains(h.getMessageId()))
						continue;
					if (getAttachmentMessageIds(txn, m.getGroupId(), h)
							.contains(m.getId())) return;
				}
			}
			// No private messages list this attachment - start the timer
			db.setCleanupTimerDuration(txn, m.getId(),
					MISSING_ATTACHMENT_CLEANUP_DURATION_MS);
			db.startCleanupTimer(txn, m.getId());
		} catch (FormatException e) {
			throw new DbException(e);
		}
		logDuration(LOG, "Receiving attachment", start);
	}

	@Override
	public void addLocalMessage(PrivateMessage m) throws DbException {
		db.transaction(false, txn -> addLocalMessage(txn, m));
	}

	@Override
	public void addLocalMessage(Transaction txn, PrivateMessage m)
			throws DbException {
		try {
			long timer = m.getAutoDeleteTimer();
			BdfDictionary meta = new BdfDictionary();
			meta.put(MSG_KEY_TIMESTAMP, m.getMessage().getTimestamp());
			meta.put(MSG_KEY_LOCAL, true);
			meta.put(MSG_KEY_READ, true);
			if (m.getFormat() != TEXT_ONLY) {
				meta.put(MSG_KEY_MSG_TYPE, PRIVATE_MESSAGE);
				meta.put(MSG_KEY_HAS_TEXT, m.hasText());
				BdfList headers = new BdfList();
				for (AttachmentHeader a : m.getAttachmentHeaders()) {
					headers.add(
							BdfList.of(a.getMessageId(), a.getContentType()));
				}
				meta.put(MSG_KEY_ATTACHMENT_HEADERS, headers);
				if (m.getFormat() == TEXT_IMAGES_AUTO_DELETE
						&& timer != NO_AUTO_DELETE_TIMER) {
					meta.put(MSG_KEY_AUTO_DELETE_TIMER, timer);
				}
			}
			// Mark attachments as shared and permanent now we're ready to send
			for (AttachmentHeader a : m.getAttachmentHeaders()) {
				GroupId g = a.getGroupId();
				for (MessageId id : getAttachmentMessageIds(txn, g, a)) {
					db.setMessageShared(txn, id);
					db.setMessagePermanent(txn, id);
				}
			}
			clientHelper.addLocalMessage(txn, m.getMessage(), meta, true,
					false);
			if (timer != NO_AUTO_DELETE_TIMER) {
				db.setCleanupTimerDuration(txn, m.getMessage().getId(), timer);
			}
			conversationManager.trackOutgoingMessage(txn, m.getMessage());
		} catch (FormatException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public AttachmentHeader addLocalAttachment(GroupId groupId, long timestamp,
			String contentType, InputStream in) throws DbException, IOException {
		return addLocalAttachment(groupId, timestamp, contentType, null, in);
	}

	@Override
	public AttachmentHeader addLocalAttachment(GroupId groupId, long timestamp,
			String contentType, @Nullable String fileName, InputStream in)
			throws DbException, IOException {
		// Spool the stream to a temporary file so we know its size without
		// loading it into memory (a large file must not be buffered in RAM).
		File tmpFile = File.createTempFile("baqqa-attachment", ".tmp");
		long totalSize;
		try {
			totalSize = spoolToFile(in, tmpFile);
		} catch (IOException e) {
			deleteQuietly(tmpFile);
			throw e;
		}
		try {
			long totalChunks = totalSize <= MAX_SINGLE_ATTACHMENT_SIZE ? 1 :
					(totalSize + MAX_ATTACHMENT_CHUNK_SIZE - 1)
							/ MAX_ATTACHMENT_CHUNK_SIZE;
			if (totalSize > MAX_TOTAL_ATTACHMENT_SIZE ||
					totalChunks > MAX_TOTAL_CHUNKS) {
				throw new FileTooBigException();
			}
			if (totalChunks == 1) {
				return storeSingleAttachment(groupId, timestamp, contentType,
						tmpFile, (int) totalSize);
			} else {
				return storeChunkedAttachment(groupId, timestamp, contentType,
						fileName, tmpFile, totalSize, (int) totalChunks);
			}
		} finally {
			deleteQuietly(tmpFile);
		}
	}

	private long spoolToFile(InputStream in, File tmpFile) throws IOException {
		try (InputStream is = in;
				OutputStream out = new FileOutputStream(tmpFile)) {
			byte[] buf = new byte[8192];
			long total = 0;
			int n;
			while ((n = is.read(buf)) != -1) {
				out.write(buf, 0, n);
				total += n;
			}
			return total;
		}
	}

	private AttachmentHeader storeSingleAttachment(GroupId groupId,
			long timestamp, String contentType, File tmpFile, int size)
			throws DbException, IOException {
		// Legacy single-message attachment: descriptor + data in one message
		byte[] descriptor =
				clientHelper.toByteArray(BdfList.of(ATTACHMENT, contentType));
		byte[] body = new byte[descriptor.length + size];
		System.arraycopy(descriptor, 0, body, 0, descriptor.length);
		try (InputStream is = new FileInputStream(tmpFile)) {
			readFully(is, body, descriptor.length, size);
		}
		if (body.length > MAX_MESSAGE_BODY_LENGTH) throw new FileTooBigException();
		BdfDictionary meta = new BdfDictionary();
		meta.put(MSG_KEY_TIMESTAMP, timestamp);
		meta.put(MSG_KEY_LOCAL, true);
		meta.put(MSG_KEY_MSG_TYPE, ATTACHMENT);
		meta.put(MSG_KEY_CONTENT_TYPE, contentType);
		meta.put(MSG_KEY_DESCRIPTOR_LENGTH, descriptor.length);
		Message m = clientHelper.createMessage(groupId, timestamp, body);
		// Mark attachments as temporary, not shared until we're ready to send
		db.transaction(false, txn ->
				clientHelper.addLocalMessage(txn, m, meta, false, true));
		return new AttachmentHeader(groupId, m.getId(), contentType);
	}

	private AttachmentHeader storeChunkedAttachment(GroupId groupId,
			long timestamp, String contentType, @Nullable String fileName,
			File tmpFile, long totalSize, int totalChunks)
			throws DbException, IOException {
		byte[] attachmentId = new byte[UniqueId.LENGTH];
		SECURE_RANDOM.nextBytes(attachmentId);
		byte[] chunkData = new byte[MAX_ATTACHMENT_CHUNK_SIZE];
		MessageId firstMessageId = null;
		try (InputStream is = new FileInputStream(tmpFile)) {
			for (int i = 0; i < totalChunks; i++) {
				int len = readChunk(is, chunkData, MAX_ATTACHMENT_CHUNK_SIZE);
				if (len < 1) throw new IOException("Empty chunk");
				// Descriptor: type, content type, attachment ID, chunk index,
				// total chunks, total size, optional file name
				BdfList descriptor = BdfList.of(ATTACHMENT, contentType,
						attachmentId, i, totalChunks, totalSize, fileName);
				byte[] descBytes = clientHelper.toByteArray(descriptor);
				byte[] body = new byte[descBytes.length + len];
				System.arraycopy(descBytes, 0, body, 0, descBytes.length);
				System.arraycopy(chunkData, 0, body, descBytes.length, len);
				if (body.length > MAX_MESSAGE_BODY_LENGTH)
					throw new FileTooBigException();
				BdfDictionary meta = new BdfDictionary();
				meta.put(MSG_KEY_TIMESTAMP, timestamp);
				meta.put(MSG_KEY_LOCAL, true);
				meta.put(MSG_KEY_MSG_TYPE, ATTACHMENT);
				meta.put(MSG_KEY_CONTENT_TYPE, contentType);
				meta.put(MSG_KEY_DESCRIPTOR_LENGTH, descBytes.length);
				meta.put(MSG_KEY_ATTACHMENT_ID, attachmentId);
				meta.put(MSG_KEY_CHUNK_INDEX, i);
				meta.put(MSG_KEY_TOTAL_CHUNKS, totalChunks);
				meta.put(MSG_KEY_TOTAL_SIZE, totalSize);
				if (fileName != null) meta.put(MSG_KEY_FILE_NAME, fileName);
				Message m = clientHelper.createMessage(groupId, timestamp, body);
				// Mark attachments as temporary, not shared until we're ready
				// to send
				db.transaction(false, txn -> clientHelper.addLocalMessage(txn,
						m, meta, false, true));
				if (i == 0) firstMessageId = m.getId();
			}
		}
		if (firstMessageId == null) throw new IOException();
		return new AttachmentHeader(groupId, firstMessageId, contentType);
	}

	private static int readChunk(InputStream in, byte[] buf, int max)
			throws IOException {
		int off = 0;
		while (off < max) {
			int n = in.read(buf, off, max - off);
			if (n == -1) break;
			off += n;
		}
		return off;
	}

	private static void readFully(InputStream in, byte[] b, int off, int len)
			throws IOException {
		while (len > 0) {
			int n = in.read(b, off, len);
			if (n == -1) throw new IOException("Unexpected end of stream");
			off += n;
			len -= n;
		}
	}

	private static void deleteQuietly(File file) {
		if (!file.delete()) file.deleteOnExit();
	}

	/**
	 * Returns the IDs of all physical messages that belong to the given
	 * (logical) attachment. For a single-message attachment this is just the
	 * referenced message; for a chunked attachment it is the message and all
	 * of its sibling chunks.
	 */
	private Collection<MessageId> getAttachmentMessageIds(Transaction txn,
			GroupId g, AttachmentHeader h) throws DbException {
		try {
			BdfDictionary meta = clientHelper
					.getMessageMetadataAsDictionary(txn, h.getMessageId());
			byte[] attachmentId = meta.getOptionalRaw(MSG_KEY_ATTACHMENT_ID);
			if (attachmentId == null) {
				List<MessageId> ids = new ArrayList<>(1);
				ids.add(h.getMessageId());
				return ids;
			}
			BdfDictionary query = BdfDictionary.of(
					new BdfEntry(MSG_KEY_MSG_TYPE, ATTACHMENT),
					new BdfEntry(MSG_KEY_ATTACHMENT_ID, attachmentId));
			return clientHelper.getMessageIds(txn, g, query);
		} catch (FormatException e) {
			throw new DbException(e);
		}
	}

	@Override
	public void removeAttachment(AttachmentHeader header) throws DbException {
		db.transaction(false, txn -> {
			for (MessageId id :
					getAttachmentMessageIds(txn, header.getGroupId(), header)) {
				db.removeMessage(txn, id);
			}
		});
	}

	private ContactId getContactId(Transaction txn, GroupId g)
			throws DbException {
		try {
			BdfDictionary meta =
					clientHelper.getGroupMetadataAsDictionary(txn, g);
			return new ContactId(meta.getInt(GROUP_KEY_CONTACT_ID));
		} catch (FormatException e) {
			throw new DbException(e);
		}
	}

	@Override
	public ContactId getContactId(GroupId g) throws DbException {
		try {
			BdfDictionary meta = clientHelper.getGroupMetadataAsDictionary(g);
			return new ContactId(meta.getInt(GROUP_KEY_CONTACT_ID));
		} catch (FormatException e) {
			throw new DbException(e);
		}
	}

	@Override
	public GroupId getConversationId(ContactId c) throws DbException {
		return db.transactionWithResult(true,
				txn -> getConversationId(txn, c));
	}

	@Override
	public GroupId getConversationId(Transaction txn, ContactId c) throws DbException {
		Contact contact = db.getContact(txn, c);
		return getContactGroup(contact).getId();
	}

	@Override
	public Collection<ConversationMessageHeader> getMessageHeaders(
			Transaction txn, ContactId c) throws DbException {
		Map<MessageId, BdfDictionary> metadata;
		Collection<MessageStatus> statuses;
		GroupId g;
		try {
			g = getContactGroup(db.getContact(txn, c)).getId();
			metadata = clientHelper.getMessageMetadataAsDictionary(txn, g);
			statuses = db.getMessageStatus(txn, c, g);
		} catch (FormatException e) {
			throw new DbException(e);
		}
		Collection<ConversationMessageHeader> headers = new ArrayList<>();
		for (MessageStatus s : statuses) {
			MessageId id = s.getMessageId();
			BdfDictionary meta = metadata.get(id);
			if (meta == null) continue;
			try {
				// Message type is null for version 0.0 private messages
				Integer messageType = meta.getOptionalInt(MSG_KEY_MSG_TYPE);
				if (messageType != null && messageType != PRIVATE_MESSAGE)
					continue;
				long timestamp = meta.getLong(MSG_KEY_TIMESTAMP);
				boolean local = meta.getBoolean(MSG_KEY_LOCAL);
				boolean read = meta.getBoolean(MSG_KEY_READ);
				if (messageType == null) {
					headers.add(new PrivateMessageHeader(id, g, timestamp,
							local, read, s.isSent(), s.isSeen(), true,
							emptyList(), NO_AUTO_DELETE_TIMER));
				} else {
					boolean hasText = meta.getBoolean(MSG_KEY_HAS_TEXT);
					long timer = meta.getLong(MSG_KEY_AUTO_DELETE_TIMER,
							NO_AUTO_DELETE_TIMER);
					headers.add(new PrivateMessageHeader(id, g, timestamp,
							local, read, s.isSent(), s.isSeen(), hasText,
							parseAttachmentHeaders(txn, g, meta), timer));
				}
			} catch (FormatException e) {
				throw new DbException(e);
			}
		}
		return headers;
	}

	@Override
	public Set<MessageId> getMessageIds(Transaction txn, ContactId c)
			throws DbException {
		GroupId g = getContactGroup(db.getContact(txn, c)).getId();
		Set<MessageId> result = new HashSet<>();
		try {
			Map<MessageId, BdfDictionary> messages =
					clientHelper.getMessageMetadataAsDictionary(txn, g);
			for (Entry<MessageId, BdfDictionary> entry : messages.entrySet()) {
				Integer type =
						entry.getValue().getOptionalInt(MSG_KEY_MSG_TYPE);
				if (type == null || type == PRIVATE_MESSAGE)
					result.add(entry.getKey());
			}
		} catch (FormatException e) {
			throw new DbException(e);
		}
		return result;
	}

	@Override
	public String getMessageText(MessageId m) throws DbException {
		return db.transactionWithNullableResult(true, txn ->
				getMessageText(txn, m));
	}

	@Override
	public String getMessageText(Transaction txn, MessageId m) throws DbException {
		try {
			BdfList body = clientHelper.getMessageAsList(txn, m);
			if (body.size() == 1) return body.getString(0); // Legacy format
			else return body.getOptionalString(1);
		} catch (FormatException e) {
			throw new DbException(e);
		}
	}

	@Override
	public PrivateMessageFormat getContactMessageFormat(Transaction txn,
			ContactId c) throws DbException {
		int minorVersion = clientVersioningManager
				.getClientMinorVersion(txn, c, CLIENT_ID, 0);
		if (minorVersion >= 3) return TEXT_IMAGES_AUTO_DELETE;
		else if (minorVersion >= 1) return TEXT_IMAGES;
		else return TEXT_ONLY;
	}

	@Override
	public DeletionResult deleteAllMessages(Transaction txn, ContactId c)
			throws DbException {
		GroupId g = getContactGroup(db.getContact(txn, c)).getId();
		// this indiscriminately deletes all raw messages in this group
		// also attachments
		for (MessageId messageId : db.getMessageIds(txn, g)) {
			db.deleteMessage(txn, messageId);
			db.deleteMessageMetadata(txn, messageId);
		}
		messageTracker.initializeGroupCount(txn, g);
		return new DeletionResult();
	}

	@Override
	public DeletionResult deleteMessages(Transaction txn, ContactId c,
			Set<MessageId> messageIds) throws DbException {
		GroupId g = getContactGroup(db.getContact(txn, c)).getId();
		for (MessageId m : messageIds) deleteMessage(txn, g, m);
		recalculateGroupCount(txn, g);
		return new DeletionResult();
	}

	@Override
	public void deleteMessages(Transaction txn, GroupId g,
			Collection<MessageId> messageIds) throws DbException {
		for (MessageId m : messageIds) deleteMessage(txn, g, m);
		recalculateGroupCount(txn, g);
		ContactId c = getContactId(txn, g);
		txn.attach(new ConversationMessagesDeletedEvent(c, messageIds));
	}

	private void deleteMessage(Transaction txn, GroupId g, MessageId m)
			throws DbException {
		try {
			BdfDictionary meta =
					clientHelper.getMessageMetadataAsDictionary(txn, m);
			Integer messageType = meta.getOptionalInt(MSG_KEY_MSG_TYPE);
			if (messageType != null && messageType == PRIVATE_MESSAGE) {
				for (AttachmentHeader h : parseAttachmentHeaders(txn, g, meta)) {
					try {
						db.deleteMessage(txn, h.getMessageId());
						db.deleteMessageMetadata(txn, h.getMessageId());
					} catch (NoSuchMessageException e) {
						// Continue
					}
				}
			}
			db.deleteMessage(txn, m);
			db.deleteMessageMetadata(txn, m);
		} catch (FormatException e) {
			throw new DbException(e);
		}
	}

	private void recalculateGroupCount(Transaction txn, GroupId g)
			throws DbException {
		try {
			Map<MessageId, BdfDictionary> metadata =
					clientHelper.getMessageMetadataAsDictionary(txn, g);
			int msgCount = 0;
			int unreadCount = 0;
			for (Entry<MessageId, BdfDictionary> entry : metadata.entrySet()) {
				BdfDictionary meta = entry.getValue();
				Integer messageType = meta.getOptionalInt(MSG_KEY_MSG_TYPE);
				if (messageType == null || messageType == PRIVATE_MESSAGE) {
					msgCount++;
					if (!meta.getBoolean(MSG_KEY_READ)) unreadCount++;
				}
			}
			messageTracker.resetGroupCount(txn, g, msgCount, unreadCount);
		} catch (FormatException e) {
			throw new DbException(e);
		}
	}
}




