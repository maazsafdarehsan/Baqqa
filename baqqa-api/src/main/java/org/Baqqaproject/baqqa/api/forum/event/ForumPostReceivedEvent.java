package org.Baqqaproject.Baqqa.api.forum.event;

import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.api.forum.ForumPostHeader;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a new forum post is received.
 */
@Immutable
@NotNullByDefault
public class ForumPostReceivedEvent extends Event {

	private final GroupId groupId;
	private final ForumPostHeader header;
	private final String text;

	public ForumPostReceivedEvent(GroupId groupId, ForumPostHeader header,
			String text) {
		this.groupId = groupId;
		this.header = header;
		this.text = text;
	}

	public GroupId getGroupId() {
		return groupId;
	}

	public ForumPostHeader getHeader() {
		return header;
	}

	public String getText() {
		return text;
	}
}




