package org.Baqqaproject.bramble.reliability;

import org.Baqqaproject.bramble.api.lifecycle.IoExecutor;
import org.Baqqaproject.bramble.api.reliability.ReliabilityLayer;
import org.Baqqaproject.bramble.api.reliability.ReliabilityLayerFactory;
import org.Baqqaproject.bramble.api.reliability.WriteHandler;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.bramble.system.SystemClock;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.concurrent.Executor;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

@Immutable
@NotNullByDefault
class ReliabilityLayerFactoryImpl implements ReliabilityLayerFactory {

	private final Executor ioExecutor;
	private final Clock clock;

	@Inject
	ReliabilityLayerFactoryImpl(@IoExecutor Executor ioExecutor) {
		this.ioExecutor = ioExecutor;
		clock = new SystemClock();
	}

	@Override
	public ReliabilityLayer createReliabilityLayer(WriteHandler writeHandler) {
		return new ReliabilityLayerImpl(ioExecutor, clock, writeHandler);
	}
}




