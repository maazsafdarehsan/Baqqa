package org.Baqqaproject.Baqqa.api.blog;

import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageVisitor;
import org.Baqqaproject.Baqqa.api.sharing.InvitationResponse;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public class BlogInvitationResponse extends InvitationResponse {

	public BlogInvitationResponse(MessageId id, GroupId groupId, long time,
			boolean local, boolean read, boolean sent, boolean seen,
			SessionId sessionId, boolean accept, GroupId shareableId,
			long autoDeleteTimer, boolean isAutoDecline) {
		super(id, groupId, time, local, read, sent, seen, sessionId,
				accept, shareableId, autoDeleteTimer, isAutoDecline);
	}

	@Override
	public <T> T accept(ConversationMessageVisitor<T> v) {
		return v.visitBlogInvitationResponse(this);
	}
}




