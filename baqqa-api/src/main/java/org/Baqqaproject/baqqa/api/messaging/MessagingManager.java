package org.Baqqaproject.Baqqa.api.messaging;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.attachment.FileTooBigException;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager.ConversationClient;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

@NotNullByDefault
public interface MessagingManager extends ConversationClient {

	/**
	 * The unique ID of the messaging client.
	 */
	ClientId CLIENT_ID = new ClientId("org.Baqqaproject.Baqqa.messaging");

	/**
	 * The current major version of the messaging client.
	 */
	int MAJOR_VERSION = 0;

	/**
	 * The current minor version of the messaging client.
	 */
	int MINOR_VERSION = 3;

	/**
	 * Stores a local private message.
	 */
	void addLocalMessage(PrivateMessage m) throws DbException;

	/**
	 * Stores a local private message.
	 */
	void addLocalMessage(Transaction txn, PrivateMessage m) throws DbException;

	/**
	 * Stores a local attachment message. If the stream is small enough it is
	 * stored as a single message, otherwise it is split into multiple chunk
	 * messages, each individually below the transport message size limit.
	 *
	 * @throws FileTooBigException If the attachment is too big
	 */
	AttachmentHeader addLocalAttachment(GroupId groupId, long timestamp,
			String contentType, InputStream is) throws DbException, IOException;

	/**
	 * Stores a local attachment message with an optional original file name.
	 * If the stream is small enough it is stored as a single message,
	 * otherwise it is split into multiple chunk messages, each individually
	 * below the transport message size limit. The returned header references
	 * the first chunk message of the (logical) attachment.
	 *
	 * @throws FileTooBigException If the attachment is too big
	 */
	AttachmentHeader addLocalAttachment(GroupId groupId, long timestamp,
			String contentType, @Nullable String fileName, InputStream is)
			throws DbException, IOException;

	/**
	 * Removes an unsent attachment.
	 */
	void removeAttachment(AttachmentHeader header) throws DbException;

	/**
	 * Returns the ID of the contact with the given private conversation.
	 */
	ContactId getContactId(GroupId g) throws DbException;

	/**
	 * Returns the ID of the private conversation with the given contact.
	 */
	GroupId getConversationId(ContactId c) throws DbException;

	/**
	 * Returns the ID of the private conversation with the given contact.
	 */
	GroupId getConversationId(Transaction txn, ContactId c) throws DbException;

	/**
	 * Returns the text of the private message with the given ID, or null if
	 * the private message has no text.
	 */
	@Nullable
	String getMessageText(MessageId m) throws DbException;

	/**
	 * Returns the text of the private message with the given ID, or null if
	 * the private message has no text.
	 */
	@Nullable
	String getMessageText(Transaction txn, MessageId m) throws DbException;

	/**
	 * Returns the private message format supported by the given contact.
	 */
	PrivateMessageFormat getContactMessageFormat(Transaction txn, ContactId c)
			throws DbException;
}




