package org.Baqqaproject.bramble.api.identity.event;

import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.identity.AuthorId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when an identity is added.
 */
@Immutable
@NotNullByDefault
public class IdentityAddedEvent extends Event {

	private final AuthorId authorId;

	public IdentityAddedEvent(AuthorId authorId) {
		this.authorId = authorId;
	}

	public AuthorId getAuthorId() {
		return authorId;
	}
}




