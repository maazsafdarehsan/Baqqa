package org.Baqqaproject.Baqqa.api.messaging.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageHeader;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a new private message is received.
 */
@Immutable
@NotNullByDefault
public class PrivateMessageReceivedEvent
		extends ConversationMessageReceivedEvent<PrivateMessageHeader> {

	public PrivateMessageReceivedEvent(PrivateMessageHeader messageHeader,
			ContactId contactId) {
		super(messageHeader, contactId);
	}

}




