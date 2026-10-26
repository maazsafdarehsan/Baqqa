package org.Baqqaproject.bramble.system;

import org.briarproject.android.dontkillmelib.wakelock.AndroidWakeLockManager;
import org.Baqqaproject.bramble.api.lifecycle.IoExecutor;
import org.Baqqaproject.bramble.api.system.WakefulIoExecutor;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;

@Module
public
class AndroidWakefulIoExecutorModule {

	@Provides
	@WakefulIoExecutor
	Executor provideWakefulIoExecutor(@IoExecutor Executor ioExecutor,
			AndroidWakeLockManager wakeLockManager) {
		return r -> wakeLockManager.executeWakefully(r, ioExecutor,
				"WakefulIoExecutor");
	}
}




