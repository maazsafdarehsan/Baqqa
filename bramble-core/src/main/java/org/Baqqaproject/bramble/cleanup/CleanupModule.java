package org.Baqqaproject.bramble.cleanup;

import org.Baqqaproject.bramble.api.cleanup.CleanupManager;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CleanupModule {

	public static class EagerSingletons {
		@Inject
		CleanupManager cleanupManager;
	}

	@Provides
	@Singleton
	CleanupManager provideCleanupManager(LifecycleManager lifecycleManager,
			EventBus eventBus, CleanupManagerImpl cleanupManager) {
		lifecycleManager.registerService(cleanupManager);
		eventBus.addListener(cleanupManager);
		return cleanupManager;
	}
}



