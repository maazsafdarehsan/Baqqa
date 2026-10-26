package org.Baqqaproject.bramble.api.network.event;

import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.network.NetworkStatus;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class NetworkStatusEvent extends Event {

	private final NetworkStatus status;

	public NetworkStatusEvent(NetworkStatus status) {
		this.status = status;
	}

	public NetworkStatus getStatus() {
		return status;
	}
}



