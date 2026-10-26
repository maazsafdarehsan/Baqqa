package org.Baqqaproject.Baqqa.api.forum.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.Baqqaproject.Baqqa.api.forum.ForumInvitationResponse;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class ForumInvitationResponseReceivedEvent extends
		ConversationMessageReceivedEvent<ForumInvitationResponse> {

	public ForumInvitationResponseReceivedEvent(
			ForumInvitationResponse response, ContactId contactId) {
		super(response, contactId);
	}

}




