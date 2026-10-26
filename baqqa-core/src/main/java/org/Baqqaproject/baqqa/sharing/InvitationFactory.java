package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.conversation.ConversationRequest;
import org.Baqqaproject.Baqqa.api.sharing.InvitationResponse;
import org.Baqqaproject.Baqqa.api.sharing.Shareable;

public interface InvitationFactory<S extends Shareable, R extends InvitationResponse> {

	ConversationRequest<S> createInvitationRequest(boolean local, boolean sent,
			boolean seen, boolean read, InviteMessage<S> m, ContactId c,
			boolean available, boolean canBeOpened, long autoDeleteTimer);

	R createInvitationResponse(MessageId id, GroupId contactGroupId, long time,
			boolean local, boolean sent, boolean seen, boolean read,
			boolean accept, GroupId shareableId, long autoDeleteTimer,
			boolean isAutoDecline);

}



