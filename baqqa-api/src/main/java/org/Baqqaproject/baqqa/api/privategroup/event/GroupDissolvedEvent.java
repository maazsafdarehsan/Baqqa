package org.Baqqaproject.Baqqa.api.privategroup.event;

import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a private group is dissolved by a remote
 * creator.
 */
@Immutable
@NotNullByDefault
public class GroupDissolvedEvent extends Event {

	private final GroupId groupId;

	public GroupDissolvedEvent(GroupId groupId) {
		this.groupId = groupId;
	}

	public GroupId getGroupId() {
		return groupId;
	}

}




