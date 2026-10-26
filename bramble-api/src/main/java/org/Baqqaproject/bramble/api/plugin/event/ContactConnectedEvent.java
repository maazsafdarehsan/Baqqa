package org.Baqqaproject.bramble.api.plugin.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.event.Event;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a contact connects that was not previously
 * connected via any transport.
 */
@Immutable
@NotNullByDefault
public class ContactConnectedEvent extends Event {

	private final ContactId contactId;

	public ContactConnectedEvent(ContactId contactId) {
		this.contactId = contactId;
	}

	public ContactId getContactId() {
		return contactId;
	}
}




