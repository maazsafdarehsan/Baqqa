package org.Baqqaproject.Baqqa.api.blog.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.blog.Blog;
import org.Baqqaproject.Baqqa.api.conversation.ConversationRequest;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class BlogInvitationRequestReceivedEvent extends
		ConversationMessageReceivedEvent<ConversationRequest<Blog>> {

	public BlogInvitationRequestReceivedEvent(ConversationRequest<Blog> request,
			ContactId contactId) {
		super(request, contactId);
	}

}




