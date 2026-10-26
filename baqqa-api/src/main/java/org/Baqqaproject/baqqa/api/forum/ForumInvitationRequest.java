package org.Baqqaproject.Baqqa.api.forum;

import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageVisitor;
import org.Baqqaproject.Baqqa.api.sharing.InvitationRequest;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class ForumInvitationRequest extends InvitationRequest<Forum> {

	public ForumInvitationRequest(MessageId id, GroupId groupId, long time,
			boolean local, boolean read, boolean sent, boolean seen,
			SessionId sessionId, Forum forum, @Nullable String text,
			boolean available, boolean canBeOpened, long autoDeleteTimer) {
		super(id, groupId, time, local, read, sent, seen, sessionId, forum,
				text, available, canBeOpened, autoDeleteTimer);
	}

	@Override
	public <T> T accept(ConversationMessageVisitor<T> v) {
		return v.visitForumInvitationRequest(this);
	}
}




