package org.Baqqaproject.bramble.api.mailbox.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.mailbox.MailboxUpdate;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Map;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when a mailbox is unpaired.
 */
@Immutable
@NotNullByDefault
public class MailboxUnpairedEvent extends Event {

	private final Map<ContactId, MailboxUpdate> localUpdates;

	public MailboxUnpairedEvent(Map<ContactId, MailboxUpdate> localUpdates) {
		this.localUpdates = localUpdates;
	}

	public Map<ContactId, MailboxUpdate> getLocalUpdates() {
		return localUpdates;
	}
}




