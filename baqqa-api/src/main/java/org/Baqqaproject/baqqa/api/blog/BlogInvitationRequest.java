package org.Baqqaproject.Baqqa.api.blog;

import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageVisitor;
import org.Baqqaproject.Baqqa.api.sharing.InvitationRequest;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;

@NotNullByDefault
public class BlogInvitationRequest extends InvitationRequest<Blog> {

	public BlogInvitationRequest(MessageId id, GroupId groupId, long time,
			boolean local, boolean read, boolean sent, boolean seen,
			SessionId sessionId, Blog blog, @Nullable String text,
			boolean available, boolean canBeOpened, long autoDeleteTimer) {
		super(id, groupId, time, local, read, sent, seen, sessionId, blog,
				text, available, canBeOpened, autoDeleteTimer);
	}

	@Override
	public <T> T accept(ConversationMessageVisitor<T> v) {
		return v.visitBlogInvitationRequest(this);
	}
}




