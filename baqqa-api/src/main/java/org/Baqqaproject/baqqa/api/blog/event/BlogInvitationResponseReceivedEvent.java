package org.Baqqaproject.Baqqa.api.blog.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.blog.BlogInvitationResponse;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class BlogInvitationResponseReceivedEvent
		extends ConversationMessageReceivedEvent<BlogInvitationResponse> {

	public BlogInvitationResponseReceivedEvent(BlogInvitationResponse response,
			ContactId contactId) {
		super(response, contactId);
	}

}




