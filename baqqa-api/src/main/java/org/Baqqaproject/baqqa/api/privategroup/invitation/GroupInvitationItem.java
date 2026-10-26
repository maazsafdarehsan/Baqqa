package org.Baqqaproject.Baqqa.api.privategroup.invitation;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.Baqqa.api.privategroup.PrivateGroup;
import org.Baqqaproject.Baqqa.api.sharing.InvitationItem;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class GroupInvitationItem extends InvitationItem<PrivateGroup> {

	private final Contact creator;

	public GroupInvitationItem(PrivateGroup privateGroup, Contact creator) {
		super(privateGroup, false);
		this.creator = creator;
	}

	public Contact getCreator() {
		return creator;
	}

}




