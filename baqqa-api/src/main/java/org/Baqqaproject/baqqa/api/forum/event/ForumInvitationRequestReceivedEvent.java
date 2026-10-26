package org.Baqqaproject.Baqqa.api.forum.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.conversation.ConversationRequest;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.Baqqaproject.Baqqa.api.forum.Forum;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class ForumInvitationRequestReceivedEvent extends
		ConversationMessageReceivedEvent<ConversationRequest<Forum>> {

	public ForumInvitationRequestReceivedEvent(ConversationRequest<Forum> request,
			ContactId contactId) {
		super(request, contactId);
	}

}




