package org.Baqqaproject.Baqqa.api.introduction;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager.ConversationClient;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;

@NotNullByDefault
public interface IntroductionManager extends ConversationClient {

	/**
	 * The unique ID of the introduction client.
	 */
	ClientId CLIENT_ID = new ClientId("org.Baqqaproject.Baqqa.introduction");

	/**
	 * The current major version of the introduction client.
	 */
	int MAJOR_VERSION = 1;

	/**
	 * Returns true if both contacts can be introduced at this moment.
	 */
	boolean canIntroduce(Contact c1, Contact c2) throws DbException;

	/**
	 * Returns true if both contacts can be introduced at this moment.
	 */
	boolean canIntroduce(Transaction txn, Contact c1, Contact c2)
			throws DbException;

	/**
	 * The current minor version of the introduction client.
	 */
	int MINOR_VERSION = 1;

	/**
	 * Sends two initial introduction messages.
	 */
	void makeIntroduction(Contact c1, Contact c2, @Nullable String text)
			throws DbException;

	/**
	 * Sends two initial introduction messages.
	 */
	void makeIntroduction(Transaction txn, Contact c1, Contact c2,
			@Nullable String text) throws DbException;

	/**
	 * Responds to an introduction.
	 */
	void respondToIntroduction(ContactId contactId, SessionId sessionId,
			boolean accept) throws DbException;

	/**
	 * Responds to an introduction.
	 */
	void respondToIntroduction(Transaction txn, ContactId contactId,
			SessionId sessionId, boolean accept) throws DbException;

}




