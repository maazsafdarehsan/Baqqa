package org.Baqqaproject.Baqqa.api.privategroup.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.identity.AuthorId;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.api.privategroup.Visibility;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class ContactRelationshipRevealedEvent extends Event {

	private final GroupId groupId;
	private final AuthorId memberId;
	private final ContactId contactId;
	private final Visibility visibility;

	public ContactRelationshipRevealedEvent(GroupId groupId, AuthorId memberId,
			ContactId contactId, Visibility visibility) {
		this.groupId = groupId;
		this.memberId = memberId;
		this.contactId = contactId;
		this.visibility = visibility;
	}

	public GroupId getGroupId() {
		return groupId;
	}

	public AuthorId getMemberId() {
		return memberId;
	}

	public ContactId getContactId() {
		return contactId;
	}

	public Visibility getVisibility() {
		return visibility;
	}

}




