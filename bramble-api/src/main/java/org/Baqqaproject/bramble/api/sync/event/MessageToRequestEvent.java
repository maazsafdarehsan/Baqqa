package org.Baqqaproject.bramble.api.sync.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.event.Event;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a message is offered by a contact and needs
 * to be requested.
 */
@Immutable
@NotNullByDefault
public class MessageToRequestEvent extends Event {

	private final ContactId contactId;

	public MessageToRequestEvent(ContactId contactId) {
		this.contactId = contactId;
	}

	public ContactId getContactId() {
		return contactId;
	}
}




