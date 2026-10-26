package org.Baqqaproject.bramble.api.mailbox.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.mailbox.MailboxUpdate;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when {@link MailboxUpdate} are received
 * from a contact.
 */
@Immutable
@NotNullByDefault
public class RemoteMailboxUpdateEvent extends Event {

	private final ContactId contactId;
	private final MailboxUpdate mailboxUpdate;

	public RemoteMailboxUpdateEvent(ContactId contactId,
			MailboxUpdate mailboxUpdate) {
		this.contactId = contactId;
		this.mailboxUpdate = mailboxUpdate;
	}

	public ContactId getContact() {
		return contactId;
	}

	public MailboxUpdate getMailboxUpdate() {
		return mailboxUpdate;
	}
}




