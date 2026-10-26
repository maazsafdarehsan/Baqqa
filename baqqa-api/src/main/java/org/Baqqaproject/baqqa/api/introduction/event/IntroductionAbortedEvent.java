package org.Baqqaproject.Baqqa.api.introduction.event;

import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class IntroductionAbortedEvent extends Event {

	private final SessionId sessionId;

	public IntroductionAbortedEvent(SessionId sessionId) {
		this.sessionId = sessionId;
	}

	public SessionId getSessionId() {
		return sessionId;
	}

}




