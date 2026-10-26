package org.Baqqaproject.Baqqa.api.avatar.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a new avatar is received.
 */
@Immutable
@NotNullByDefault
public class AvatarUpdatedEvent extends Event {

	private final ContactId contactId;
	private final AttachmentHeader attachmentHeader;

	public AvatarUpdatedEvent(ContactId contactId,
			AttachmentHeader attachmentHeader) {
		this.contactId = contactId;
		this.attachmentHeader = attachmentHeader;
	}

	public ContactId getContactId() {
		return contactId;
	}

	public AttachmentHeader getAttachmentHeader() {
		return attachmentHeader;
	}
}




