package org.Baqqaproject.Baqqa.api.privategroup.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationRequest;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class GroupInvitationRequestReceivedEvent extends
		ConversationMessageReceivedEvent<GroupInvitationRequest> {

	public GroupInvitationRequestReceivedEvent(GroupInvitationRequest request,
			ContactId contactId) {
		super(request, contactId);
	}

}




