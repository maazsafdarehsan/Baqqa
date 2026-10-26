package org.Baqqaproject.bramble.api.cleanup.event;

import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a message's cleanup timer is started.
 */
@Immutable
@NotNullByDefault
public class CleanupTimerStartedEvent extends Event {

	private final MessageId messageId;
	private final long cleanupDeadline;

	public CleanupTimerStartedEvent(MessageId messageId,
			long cleanupDeadline) {
		this.messageId = messageId;
		this.cleanupDeadline = cleanupDeadline;
	}

	public MessageId getMessageId() {
		return messageId;
	}

	public long getCleanupDeadline() {
		return cleanupDeadline;
	}
}




