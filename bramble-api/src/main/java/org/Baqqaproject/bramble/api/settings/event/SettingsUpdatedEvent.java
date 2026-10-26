package org.Baqqaproject.bramble.api.settings.event;

import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.settings.Settings;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

/**
 * An event that is broadcast when one or more settings are updated.
 */
@Immutable
@NotNullByDefault
public class SettingsUpdatedEvent extends Event {

	private final String namespace;
	private final Settings settings;

	public SettingsUpdatedEvent(String namespace, Settings settings) {
		this.namespace = namespace;
		this.settings = settings;
	}

	public String getNamespace() {
		return namespace;
	}

	public Settings getSettings() {
		return settings;
	}
}




