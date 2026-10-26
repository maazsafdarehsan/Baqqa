package org.Baqqaproject.Baqqa.api.messaging.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a new attachment is received.
 */
@Immutable
@NotNullByDefault
public class AttachmentReceivedEvent extends Event {

	private final MessageId messageId;
	private final ContactId contactId;

	public AttachmentReceivedEvent(MessageId messageId, ContactId contactId) {
		this.messageId = messageId;
		this.contactId = contactId;
	}

	public MessageId getMessageId() {
		return messageId;
	}

	public ContactId getContactId() {
		return contactId;
	}
}




