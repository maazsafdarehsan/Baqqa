package org.Baqqaproject.Baqqa.android.privategroup.memberlist;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo.Status;
import org.Baqqaproject.Baqqa.api.privategroup.GroupMember;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
@NotNullByDefault
class MemberListItem {

	private final GroupMember groupMember;
	private boolean online;

	MemberListItem(GroupMember groupMember, boolean online) {
		this.groupMember = groupMember;
		this.online = online;
	}

	Author getMember() {
		return groupMember.getAuthor();
	}

	AuthorInfo getAuthorInfo() {
		return groupMember.getAuthorInfo();
	}

	Status getStatus() {
		return groupMember.getAuthorInfo().getStatus();
	}

	boolean isCreator() {
		return groupMember.isCreator();
	}

	@Nullable
	ContactId getContactId() {
		return groupMember.getContactId();
	}

	boolean isOnline() {
		return online;
	}

	void setOnline(boolean online) {
		this.online = online;
	}

}




