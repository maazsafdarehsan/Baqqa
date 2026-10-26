package org.Baqqaproject.bramble.plugin;

import org.Baqqaproject.bramble.api.plugin.Backoff;
import org.Baqqaproject.bramble.api.plugin.BackoffFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
class BackoffFactoryImpl implements BackoffFactory {

	@Override
	public Backoff createBackoff(int minInterval, int maxInterval,
			double base) {
		return new BackoffImpl(minInterval, maxInterval, base);
	}
}




