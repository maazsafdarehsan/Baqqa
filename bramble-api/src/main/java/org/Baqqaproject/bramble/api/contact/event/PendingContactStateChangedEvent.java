package org.Baqqaproject.bramble.api.contact.event;

import org.Baqqaproject.bramble.api.contact.PendingContactId;
import org.Baqqaproject.bramble.api.contact.PendingContactState;
import org.Baqqaproject.bramble.api.event.Event;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a pending contact's state is changed.
 */
@Immutable
@NotNullByDefault
public class PendingContactStateChangedEvent extends Event {

	private final PendingContactId id;
	private final PendingContactState state;

	public PendingContactStateChangedEvent(PendingContactId id,
			PendingContactState state) {
		this.id = id;
		this.state = state;
	}

	public PendingContactId getId() {
		return id;
	}

	public PendingContactState getPendingContactState() {
		return state;
	}

}




