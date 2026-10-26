package org.Baqqaproject.Baqqa.api.privategroup.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationResponse;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class GroupInvitationResponseReceivedEvent
		extends ConversationMessageReceivedEvent<GroupInvitationResponse> {

	public GroupInvitationResponseReceivedEvent(
			GroupInvitationResponse response, ContactId contactId) {
		super(response, contactId);
	}
}




