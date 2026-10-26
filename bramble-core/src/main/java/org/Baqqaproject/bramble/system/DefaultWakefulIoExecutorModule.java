package org.Baqqaproject.bramble.system;

import org.Baqqaproject.bramble.api.lifecycle.IoExecutor;
import org.Baqqaproject.bramble.api.system.WakefulIoExecutor;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;

/**
 * Provides a default implementation of {@link WakefulIoExecutor} for systems
 * without wake locks.
 */
@Module
public class DefaultWakefulIoExecutorModule {

	@Provides
	@WakefulIoExecutor
	Executor provideWakefulIoExecutor(@IoExecutor Executor ioExecutor) {
		return ioExecutor;
	}
}



