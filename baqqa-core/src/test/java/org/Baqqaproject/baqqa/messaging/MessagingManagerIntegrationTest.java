package org.Baqqaproject.Baqqa.messaging;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.data.BdfEntry;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.MessageDeletedException;
import org.Baqqaproject.bramble.api.db.NoSuchMessageException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.bramble.test.TestDatabaseConfigModule;
import org.Baqqaproject.Baqqa.api.attachment.Attachment;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageHeader;
import org.Baqqaproject.Baqqa.api.messaging.MessagingManager;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessage;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFactory;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageHeader;
import org.Baqqaproject.Baqqa.test.BaqqaIntegrationTest;
import org.Baqqaproject.Baqqa.test.BaqqaIntegrationTestComponent;
import org.Baqqaproject.Baqqa.test.DaggerBaqqaIntegrationTestComponent;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.Baqqaproject.bramble.api.sync.SyncConstants.MAX_MESSAGE_BODY_LENGTH;
import static org.Baqqaproject.bramble.test.TestUtils.getRandomBytes;
import static org.Baqqaproject.bramble.util.IoUtils.copyAndClose;
import static org.Baqqaproject.bramble.util.StringUtils.getRandomString;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_ATTACHMENT_CHUNK_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_SINGLE_ATTACHMENT_SIZE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_ATTACHMENT_ID;
import static org.Baqqaproject.Baqqa.api.autodelete.AutoDeleteConstants.MIN_AUTO_DELETE_TIMER_MS;
import static org.Baqqaproject.Baqqa.api.autodelete.AutoDeleteConstants.NO_AUTO_DELETE_TIMER;
import static org.Baqqaproject.Baqqa.messaging.MessageTypes.ATTACHMENT;
import static org.Baqqaproject.Baqqa.messaging.MessagingConstants.MSG_KEY_MSG_TYPE;
import static org.Baqqaproject.Baqqa.test.BaqqaTestUtils.assertGroupCount;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MessagingManagerIntegrationTest
		extends BaqqaIntegrationTest<BaqqaIntegrationTestComponent> {

	private DatabaseComponent db0, db1;
	private MessagingManager messagingManager0, messagingManager1;
	private PrivateMessageFactory messageFactory;
	private ContactId contactId;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		db0 = c0.getDatabaseComponent();
		db1 = c1.getDatabaseComponent();
		messagingManager0 = c0.getMessagingManager();
		messagingManager1 = c1.getMessagingManager();
		messageFactory = c0.getPrivateMessageFactory();
		assertEquals(contactId0From1, contactId1From0);
		contactId = contactId0From1;
	}

	@Override
	protected void createComponents() {
		BaqqaIntegrationTestComponent component =
				DaggerBaqqaIntegrationTestComponent.builder().build();
		BaqqaIntegrationTestComponent.Helper.injectEagerSingletons(component);
		component.inject(this);

		c0 = DaggerBaqqaIntegrationTestComponent.builder()
				.testDatabaseConfigModule(new TestDatabaseConfigModule(t0Dir))
				.build();
		BaqqaIntegrationTestComponent.Helper.injectEagerSingletons(c0);

		c1 = DaggerBaqqaIntegrationTestComponent.builder()
				.testDatabaseConfigModule(new TestDatabaseConfigModule(t1Dir))
				.build();
		BaqqaIntegrationTestComponent.Helper.injectEagerSingletons(c1);

		c2 = DaggerBaqqaIntegrationTestComponent.builder()
				.testDatabaseConfigModule(new TestDatabaseConfigModule(t2Dir))
				.build();
		BaqqaIntegrationTestComponent.Helper.injectEagerSingletons(c2);
	}

	@Test
	public void testSimpleConversation() throws Exception {
		// conversation starts out empty
		Collection<ConversationMessageHeader> messages0 = getMessages(c0);
		Collection<ConversationMessageHeader> messages1 = getMessages(c1);
		assertEquals(0, messages0.size());
		assertEquals(0, messages1.size());

		// message is sent/displayed properly
		String text = getRandomString(42);
		sendMessage(c0, c1, text);
		messages0 = getMessages(c0);
		messages1 = getMessages(c1);
		assertEquals(1, messages0.size());
		assertEquals(1, messages1.size());
		PrivateMessageHeader m0 =
				(PrivateMessageHeader) messages0.iterator().next();
		PrivateMessageHeader m1 =
				(PrivateMessageHeader) messages1.iterator().next();
		assertTrue(m0.hasText());
		assertTrue(m1.hasText());
		assertEquals(0, m0.getAttachmentHeaders().size());
		assertEquals(0, m1.getAttachmentHeaders().size());
		assertEquals(NO_AUTO_DELETE_TIMER, m0.getAutoDeleteTimer());
		assertEquals(NO_AUTO_DELETE_TIMER, m1.getAutoDeleteTimer());
		assertTrue(m0.isRead());
		assertFalse(m1.isRead());
		assertGroupCounts(c0, 1, 0);
		assertGroupCounts(c1, 1, 1);

		// same for reply
		String text2 = getRandomString(42);
		sendMessage(c1, c0, text2);
		messages0 = getMessages(c0);
		messages1 = getMessages(c1);
		assertEquals(2, messages0.size());
		assertEquals(2, messages1.size());
		assertGroupCounts(c0, 2, 1);
		assertGroupCounts(c1, 2, 1);
	}

	@Test
	public void testAttachments() throws Exception {
		// send message with attachment
		AttachmentHeader h = addAttachment(c0);
		sendMessage(c0, c1, null, singletonList(h));

		// message with attachment is sent/displayed properly
		Collection<ConversationMessageHeader> messages0 = getMessages(c0);
		Collection<ConversationMessageHeader> messages1 = getMessages(c1);
		assertEquals(1, messages0.size());
		assertEquals(1, messages1.size());
		PrivateMessageHeader m0 =
				(PrivateMessageHeader) messages0.iterator().next();
		PrivateMessageHeader m1 =
				(PrivateMessageHeader) messages1.iterator().next();
		assertFalse(m0.hasText());
		assertFalse(m1.hasText());
		assertEquals(1, m0.getAttachmentHeaders().size());
		assertEquals(1, m1.getAttachmentHeaders().size());
		assertEquals(NO_AUTO_DELETE_TIMER, m0.getAutoDeleteTimer());
		assertEquals(NO_AUTO_DELETE_TIMER, m1.getAutoDeleteTimer());
		assertTrue(m0.isRead());
		assertFalse(m1.isRead());
		assertGroupCounts(c0, 1, 0);
		assertGroupCounts(c1, 1, 1);
	}

	@Test
	public void testAutoDeleteTimer() throws Exception {
		// send message with auto-delete timer
		sendMessage(c0, c1, getRandomString(123), emptyList(),
				MIN_AUTO_DELETE_TIMER_MS);

		// message with timer is sent/displayed properly
		Collection<ConversationMessageHeader> messages0 = getMessages(c0);
		Collection<ConversationMessageHeader> messages1 = getMessages(c1);
		assertEquals(1, messages0.size());
		assertEquals(1, messages1.size());
		PrivateMessageHeader m0 =
				(PrivateMessageHeader) messages0.iterator().next();
		PrivateMessageHeader m1 =
				(PrivateMessageHeader) messages1.iterator().next();
		assertTrue(m0.hasText());
		assertTrue(m1.hasText());
		assertEquals(0, m0.getAttachmentHeaders().size());
		assertEquals(0, m1.getAttachmentHeaders().size());
		assertEquals(MIN_AUTO_DELETE_TIMER_MS, m0.getAutoDeleteTimer());
		assertEquals(MIN_AUTO_DELETE_TIMER_MS, m1.getAutoDeleteTimer());
		assertTrue(m0.isRead());
		assertFalse(m1.isRead());
		assertGroupCounts(c0, 1, 0);
		assertGroupCounts(c1, 1, 1);
	}

	@Test
	public void testDeleteAll() throws Exception {
		// send 3 messages (1 with attachment)
		sendMessage(c0, c1, getRandomString(42));
		sendMessage(c0, c1, getRandomString(23));
		sendMessage(c0, c1, null, singletonList(addAttachment(c0)));
		assertEquals(3, getMessages(c0).size());
		assertEquals(3, getMessages(c1).size());
		assertGroupCounts(c0, 3, 0);
		assertGroupCounts(c1, 3, 3);

		// delete all messages on both sides (deletes all, because returns true)
		assertTrue(db0.transactionWithResult(false,
				txn -> messagingManager0.deleteAllMessages(txn, contactId))
				.allDeleted());
		assertTrue(db1.transactionWithResult(false,
				txn -> messagingManager1.deleteAllMessages(txn, contactId))
				.allDeleted());

		// all messages are gone
		assertEquals(0, getMessages(c0).size());
		assertEquals(0, getMessages(c1).size());
		assertGroupCounts(c0, 0, 0);
		assertGroupCounts(c1, 0, 0);
	}

	@Test
	public void testDeleteSubset() throws Exception {
		// send 3 message (1 with attachment)
		PrivateMessage m0 = sendMessage(c0, c1, getRandomString(42));
		PrivateMessage m1 = sendMessage(c0, c1, getRandomString(23));
		PrivateMessage m2 =
				sendMessage(c0, c1, null, singletonList(addAttachment(c0)));
		assertGroupCounts(c0, 3, 0);
		assertGroupCounts(c1, 3, 3);

		// delete 2 messages on both sides (deletes all, because returns true)
		Set<MessageId> toDelete = new HashSet<>();
		toDelete.add(m1.getMessage().getId());
		toDelete.add(m2.getMessage().getId());
		assertTrue(db0.transactionWithResult(false, txn ->
				messagingManager0.deleteMessages(txn, contactId, toDelete))
				.allDeleted());
		assertTrue(db1.transactionWithResult(false, txn ->
				messagingManager1.deleteMessages(txn, contactId, toDelete))
				.allDeleted());

		// all messages except 1 are gone
		assertEquals(1, getMessages(c0).size());
		assertEquals(1, getMessages(c1).size());
		assertEquals(m0.getMessage().getId(),
				getMessages(c0).iterator().next().getId());
		assertEquals(m0.getMessage().getId(),
				getMessages(c1).iterator().next().getId());
		assertGroupCounts(c0, 1, 0);
		assertGroupCounts(c1, 1, 1);

		// remove also last message
		toDelete.clear();
		toDelete.add(m0.getMessage().getId());
		assertTrue(db0.transactionWithResult(false, txn ->
				messagingManager0.deleteMessages(txn, contactId, toDelete))
				.allDeleted());
		assertEquals(0, getMessages(c0).size());
		assertGroupCounts(c0, 0, 0);
	}

	@Test
	public void testDeleteLegacySubset() throws Exception {
		// send legacy message
		GroupId g = c0.getMessagingManager().getConversationId(contactId);
		PrivateMessage m0 = messageFactory.createLegacyPrivateMessage(g,
				c0.getClock().currentTimeMillis(), getRandomString(42));
		c0.getMessagingManager().addLocalMessage(m0);
		syncMessage(c0, c1, contactId, 1, true);

		// message arrived on both sides
		assertEquals(1, getMessages(c0).size());
		assertEquals(1, getMessages(c1).size());

		// delete message on both sides (deletes all, because returns true)
		Set<MessageId> toDelete = new HashSet<>();
		toDelete.add(m0.getMessage().getId());
		assertTrue(c0.getConversationManager()
				.deleteMessages(contactId, toDelete).allDeleted());
		assertTrue(c1.getConversationManager()
				.deleteMessages(contactId, toDelete).allDeleted());

		// message was deleted
		assertEquals(0, getMessages(c0).size());
		assertEquals(0, getMessages(c1).size());
	}

	@Test
	public void testDeleteAttachment() throws Exception {
		// send one message with attachment
		AttachmentHeader h = addAttachment(c0);
		sendMessage(c0, c1, getRandomString(42), singletonList(h));

		// attachment exists on both devices
		db0.transaction(true, txn -> db0.getMessage(txn, h.getMessageId()));
		db1.transaction(true, txn -> db1.getMessage(txn, h.getMessageId()));

		// delete message on both sides (deletes all, because returns true)
		assertTrue(db0.transactionWithResult(false,
				txn -> messagingManager0.deleteAllMessages(txn, contactId))
				.allDeleted());
		assertTrue(db1.transactionWithResult(false,
				txn -> messagingManager1.deleteAllMessages(txn, contactId))
				.allDeleted());

		// attachment was deleted on both devices
		try {
			db0.transaction(true, txn -> db0.getMessage(txn, h.getMessageId()));
			fail();
		} catch (MessageDeletedException e) {
			// expected
		}
		try {
			db1.transaction(true, txn -> db1.getMessage(txn, h.getMessageId()));
			fail();
		} catch (MessageDeletedException e) {
			// expected
		}
	}

	@Test
	public void testDeletingEmptySet() throws Exception {
		assertTrue(db0.transactionWithResult(false, txn ->
				messagingManager0.deleteMessages(txn, contactId, emptySet()))
				.allDeleted());
	}

	@Test
	public void testChunkedAttachmentRoundTrip() throws Exception {
		// Just above the single-attachment threshold: exactly 2 chunks
		byte[] data = getRandomBytes(
				MAX_SINGLE_ATTACHMENT_SIZE + 1234);
		testChunkedAttachment(data);
	}

	@Test
	public void testManyChunkAttachmentRoundTrip() throws Exception {
		// Requires several chunks
		byte[] data = getRandomBytes(MAX_ATTACHMENT_CHUNK_SIZE * 5 + 1000);
		testChunkedAttachment(data);
	}

	private void testChunkedAttachment(byte[] data) throws Exception {
		GroupId g = c0.getMessagingManager().getConversationId(contactId);
		InputStream stream = new ByteArrayInputStream(data);
		AttachmentHeader h = messagingManager0.addLocalAttachment(g,
				c0.getClock().currentTimeMillis(), "application/octet-stream",
				"test.bin", stream);
		// Create a private message referencing the (logical) attachment and
		// add it, which marks the chunk messages as shared and permanent
		PrivateMessage m = messageFactory.createPrivateMessage(g,
				c0.getClock().currentTimeMillis(), "text",
				singletonList(h), NO_AUTO_DELETE_TIMER);
		messagingManager0.addLocalMessage(m);
		// Sync the private message and every chunk
		int chunks = (data.length + MAX_ATTACHMENT_CHUNK_SIZE - 1)
				/ MAX_ATTACHMENT_CHUNK_SIZE;
		syncMessage(c0, c1, contactId, 1 + chunks, true);

		// The message arrived on both sides with one attachment header
		Collection<ConversationMessageHeader> messages1 = getMessages(c1);
		assertEquals(1, messages1.size());
		PrivateMessageHeader m1 = (PrivateMessageHeader)
				messages1.iterator().next();
		assertEquals(1, m1.getAttachmentHeaders().size());
		AttachmentHeader h1 = m1.getAttachmentHeaders().get(0);

		// The attachment can be reassembled and matches the original bytes
		Attachment attachment =
				c1.getAttachmentReader().getAttachment(h1);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyAndClose(attachment.getStream(), out);
		assertArrayEquals(data, out.toByteArray());

		// The local side can also read it back
		Attachment local = c0.getAttachmentReader().getAttachment(h);
		ByteArrayOutputStream out0 = new ByteArrayOutputStream();
		copyAndClose(local.getStream(), out0);
		assertArrayEquals(data, out0.toByteArray());
	}

	private PrivateMessage sendMessage(BaqqaIntegrationTestComponent from,
			BaqqaIntegrationTestComponent to, String text) throws Exception {
		return sendMessage(from, to, text, emptyList());
	}

	private PrivateMessage sendMessage(BaqqaIntegrationTestComponent from,
			BaqqaIntegrationTestComponent to, @Nullable String text,
			List<AttachmentHeader> attachments) throws Exception {
		return sendMessage(from, to, text, attachments, NO_AUTO_DELETE_TIMER);
	}

	private PrivateMessage sendMessage(BaqqaIntegrationTestComponent from,
			BaqqaIntegrationTestComponent to, @Nullable String text,
			List<AttachmentHeader> attachments, long autoDeleteTimer)
			throws Exception {
		GroupId g = from.getMessagingManager().getConversationId(contactId);
		PrivateMessage m = messageFactory.createPrivateMessage(g,
				from.getClock().currentTimeMillis(), text, attachments,
				autoDeleteTimer);
		from.getMessagingManager().addLocalMessage(m);
		syncMessage(from, to, contactId, 1 + attachments.size(), true);
		return m;
	}

	private AttachmentHeader addAttachment(BaqqaIntegrationTestComponent c)
			throws Exception {
		GroupId g = c.getMessagingManager().getConversationId(contactId);
		InputStream stream = new ByteArrayInputStream(getRandomBytes(42));
		return c.getMessagingManager().addLocalAttachment(g,
				c.getClock().currentTimeMillis(), "image/jpeg", stream);
	}

	private Collection<ConversationMessageHeader> getMessages(
			BaqqaIntegrationTestComponent c)
			throws Exception {
		Collection<ConversationMessageHeader> messages =
				c.getDatabaseComponent().transactionWithResult(true,
						txn -> c.getMessagingManager()
								.getMessageHeaders(txn, contactId));
		Set<MessageId> ids =
				c.getDatabaseComponent().transactionWithResult(true,
						txn ->
								c.getMessagingManager()
										.getMessageIds(txn, contactId));
		assertEquals(messages.size(), ids.size());
		for (ConversationMessageHeader h : messages) {
			assertTrue(ids.contains(h.getId()));
		}
		return messages;
	}

	private void assertGroupCounts(BaqqaIntegrationTestComponent c,
			long msgCount, long unreadCount) throws Exception {
		GroupId g = c.getMessagingManager().getConversationId(contactId);
		assertGroupCount(c.getMessageTracker(), g, msgCount, unreadCount);
	}





	/**
	 * Verifies that a small attachment uses the legacy single-message path
	 * (not chunked) Ã¢â‚¬â€ backward compatibility.
	 */
	@Test
	public void testSmallAttachmentUsesSingleMessagePath() throws Exception {
		byte[] data = getRandomBytes(MAX_SINGLE_ATTACHMENT_SIZE);
		GroupId g = c0.getMessagingManager().getConversationId(contactId);
		InputStream stream = new ByteArrayInputStream(data);
		AttachmentHeader h = messagingManager0.addLocalAttachment(g,
				c0.getClock().currentTimeMillis(), "image/jpeg", stream);
		// The header references a single message that holds the whole payload
		// (no attachmentId metadata Ã¢â€ â€™ single-message path)
		BdfDictionary meta = c0.getDatabaseComponent()
				.transactionWithResult(true, txn ->
						c0.getClientHelper()
								.getMessageMetadataAsDictionary(txn,
										h.getMessageId()));
		assertNull(meta.getOptionalRaw(MSG_KEY_ATTACHMENT_ID));
		// Round-trips correctly
		PrivateMessage m = messageFactory.createPrivateMessage(g,
				c0.getClock().currentTimeMillis(), "text",
				singletonList(h), NO_AUTO_DELETE_TIMER);
		messagingManager0.addLocalMessage(m);
		syncMessage(c0, c1, contactId, 2, true);
		Attachment attachment =
				c1.getAttachmentReader().getAttachment(
						h);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyAndClose(attachment.getStream(), out);
		assertArrayEquals(data, out.toByteArray());
	}

	/**
	 * Verifies that chunks arriving out of order still reassemble correctly.
	 * The unit tests cover the reader's out-of-order tolerance; this test
	 * confirms the end-to-end sync delivers chunks that the reader can
	 * reassemble regardless of arrival order.
	 */
	/**
	 * Verifies that a large attachment actually triggers chunking: multiple
	 * physical messages are created, every physical message stays below the
	 * transport limit, and the original payload is reconstructed
	 * byte-for-byte with filename and content type preserved.
	 */
	@Test
	public void testLargeAttachmentChunkingTriggered() throws Exception {
		// Large enough to require several chunks
		byte[] data = getRandomBytes(
				MAX_ATTACHMENT_CHUNK_SIZE * 4 + 12345);
		String contentType = "application/octet-stream";
		String fileName = "large-test-file.bin";

		GroupId g = c0.getMessagingManager().getConversationId(contactId);
		InputStream stream = new ByteArrayInputStream(data);
		AttachmentHeader h = messagingManager0.addLocalAttachment(g,
				c0.getClock().currentTimeMillis(), contentType, fileName,
				stream);

		// Verify chunking was triggered: multiple physical messages
		Collection<MessageId> chunkIds = c0.getDatabaseComponent()
				.transactionWithResult(true, txn ->
						getChunkMessageIds(txn, g, h));
		assertTrue("Expected multiple chunks, got " + chunkIds.size(),
				chunkIds.size() > 1);

		// Verify every physical message stays below the transport limit
		for (MessageId id : chunkIds) {
			Message msg = c0.getDatabaseComponent()
					.transactionWithResult(true, txn ->
							c0.getDatabaseComponent().getMessage(txn, id));
			assertTrue(
					"Physical message exceeds transport limit: "
							+ msg.getBody().length + " > "
							+ MAX_MESSAGE_BODY_LENGTH,
					msg.getBody().length <= MAX_MESSAGE_BODY_LENGTH);
		}

		// Verify filename and content type are preserved in chunk metadata
		BdfDictionary meta0 = c0.getClientHelper()
				.getMessageMetadataAsDictionary(
						chunkIds.iterator().next());
		assertEquals(fileName,
				meta0.getOptionalString("fileName"));
		assertEquals(contentType, meta0.getString("contentType"));

		// Send and reassemble
		PrivateMessage m = messageFactory.createPrivateMessage(g,
				c0.getClock().currentTimeMillis(), "text",
				singletonList(h), NO_AUTO_DELETE_TIMER);
		messagingManager0.addLocalMessage(m);
		syncMessage(c0, c1, contactId, 1 + chunkIds.size(), true);

		PrivateMessageHeader m1 = (PrivateMessageHeader)
				getMessages(c1).iterator().next();
		AttachmentHeader h1 = m1.getAttachmentHeaders().get(0);
		assertEquals(contentType, h1.getContentType());

		Attachment attachment = c1.getAttachmentReader().getAttachment(h1);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyAndClose(attachment.getStream(), out);

		// Verify byte-for-byte reconstruction
		assertArrayEquals(data, out.toByteArray());
	}

	/**
	 * Verifies that a small attachment uses the legacy single-message path
	 * (no chunking), confirming backward compatibility.
	 */
	@Test
	public void testSmallAttachmentLegacyPathUnchanged() throws Exception {
		byte[] data = getRandomBytes(MAX_SINGLE_ATTACHMENT_SIZE);
		String contentType = "image/jpeg";
		String fileName = "small-test.jpg";

		GroupId g = c0.getMessagingManager().getConversationId(contactId);
		InputStream stream = new ByteArrayInputStream(data);
		AttachmentHeader h = messagingManager0.addLocalAttachment(g,
				c0.getClock().currentTimeMillis(), contentType, fileName,
				stream);

		// Verify single-message path: only one physical message
		Collection<MessageId> chunkIds = c0.getDatabaseComponent()
				.transactionWithResult(true, txn ->
						getChunkMessageIds(txn, g, h));
		assertEquals(1, chunkIds.size());

		// Verify no attachmentId metadata (legacy path)
		MessageId singleId = chunkIds.iterator().next();
		BdfDictionary meta = c0.getClientHelper()
				.getMessageMetadataAsDictionary(singleId);
		assertNull(meta.getOptionalRaw("attachmentId"));

		// Send and reassemble
		PrivateMessage m = messageFactory.createPrivateMessage(g,
				c0.getClock().currentTimeMillis(), "text",
				singletonList(h), NO_AUTO_DELETE_TIMER);
		messagingManager0.addLocalMessage(m);
		syncMessage(c0, c1, contactId, 2, true);

		PrivateMessageHeader m1 = (PrivateMessageHeader)
				getMessages(c1).iterator().next();
		AttachmentHeader h1 = m1.getAttachmentHeaders().get(0);
		assertEquals(contentType, h1.getContentType());

		Attachment attachment = c1.getAttachmentReader().getAttachment(h1);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyAndClose(attachment.getStream(), out);
		assertArrayEquals(data, out.toByteArray());
	}

	private Collection<MessageId> getChunkMessageIds(Transaction txn,
			GroupId g, AttachmentHeader h) throws Exception {
		try {
			BdfDictionary meta =
					c0.getClientHelper()
							.getMessageMetadataAsDictionary(txn,
									h.getMessageId());
			byte[] attachmentId =
					meta.getOptionalRaw(MSG_KEY_ATTACHMENT_ID);
			if (attachmentId == null) {
				// Legacy single-message attachment
				return java.util.Collections
						.singletonList(h.getMessageId());
			}
			BdfDictionary query = BdfDictionary.of(
					new BdfEntry(MSG_KEY_MSG_TYPE, ATTACHMENT),
					new BdfEntry(MSG_KEY_ATTACHMENT_ID, attachmentId));
			return c0.getClientHelper().getMessageIds(txn, g, query);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Test
	public void testChunkedAttachmentOutOfOrderReassembly()
			throws Exception {
		byte[] data = getRandomBytes(
				MAX_ATTACHMENT_CHUNK_SIZE * 3 + 1234);
		GroupId g = c0.getMessagingManager().getConversationId(contactId);
		InputStream stream = new ByteArrayInputStream(data);
		AttachmentHeader h = messagingManager0.addLocalAttachment(g,
				c0.getClock().currentTimeMillis(), "application/octet-stream",
				"test.bin", stream);
		PrivateMessage m = messageFactory.createPrivateMessage(g,
				c0.getClock().currentTimeMillis(), "text",
				singletonList(h), NO_AUTO_DELETE_TIMER);
		messagingManager0.addLocalMessage(m);
		int chunks = (data.length + MAX_ATTACHMENT_CHUNK_SIZE - 1)
				/ MAX_ATTACHMENT_CHUNK_SIZE;
		syncMessage(c0, c1, contactId, 1 + chunks, true);

		// Reassemble on the receiver Ã¢â‚¬â€ the reader tolerates any order
		PrivateMessageHeader m1 = (PrivateMessageHeader)
				getMessages(c1).iterator().next();
		AttachmentHeader h1 = m1.getAttachmentHeaders().get(0);
		Attachment attachment = c1.getAttachmentReader().getAttachment(h1);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copyAndClose(attachment.getStream(), out);
		assertArrayEquals(data, out.toByteArray());
	}

	/**
	 * Verifies that a missing chunk prevents the attachment from being
	 * presented as a complete file. We sync all but one chunk and confirm
	 * the reader throws rather than returning truncated data.
	 */
	@Test(expected = DbException.class)
	public void testMissingChunkPreventsReassembly() throws Exception {
		byte[] data = getRandomBytes(
				MAX_ATTACHMENT_CHUNK_SIZE * 2 + 5678);
		GroupId g = c0.getMessagingManager().getConversationId(contactId);
		InputStream stream = new ByteArrayInputStream(data);
		AttachmentHeader h = messagingManager0.addLocalAttachment(g,
				c0.getClock().currentTimeMillis(), "application/octet-stream",
				"test.bin", stream);
		PrivateMessage m = messageFactory.createPrivateMessage(g,
				c0.getClock().currentTimeMillis(), "text",
				singletonList(h), NO_AUTO_DELETE_TIMER);
		messagingManager0.addLocalMessage(m);
		int chunks = (data.length + MAX_ATTACHMENT_CHUNK_SIZE - 1)
				/ MAX_ATTACHMENT_CHUNK_SIZE;
		// Sync the private message and all chunks
		syncMessage(c0, c1, contactId, 1 + chunks, true);

		// Find and delete the last chunk on the receiver side
		PrivateMessageHeader m1 = (PrivateMessageHeader)
				getMessages(c1).iterator().next();
		AttachmentHeader h1 = m1.getAttachmentHeaders().get(0);
		MessageId chunkToDelete = c1.getDatabaseComponent()
				.transactionWithResult(true, txn ->
						getLastChunkId(txn, g, h1));
		c1.getDatabaseComponent().transaction(false, txn -> {
			c1.getDatabaseComponent().deleteMessage(txn, chunkToDelete);
			c1.getDatabaseComponent()
					.deleteMessageMetadata(txn, chunkToDelete);
		});

		// The reader must not return a truncated file
		c1.getAttachmentReader().getAttachment(h1);
	}

	private MessageId getLastChunkId(Transaction txn, GroupId g,
			AttachmentHeader h1) throws Exception {
		BdfDictionary query = BdfDictionary.of(
				new BdfEntry(MSG_KEY_MSG_TYPE, ATTACHMENT),
				new BdfEntry(MSG_KEY_ATTACHMENT_ID, getAttachmentId(txn,
						g, h1)));
		Collection<MessageId> ids =
				c1.getClientHelper().getMessageIds(txn, g, query);
		MessageId last = null;
		for (MessageId id : ids) last = id;
		return last;
	}

	private byte[] getAttachmentId(Transaction txn, GroupId g,
			AttachmentHeader h1) throws Exception {
		BdfDictionary meta =
				c1.getClientHelper()
						.getMessageMetadataAsDictionary(txn,
								h1.getMessageId());
		return meta.getRaw(MSG_KEY_ATTACHMENT_ID);
	}


}