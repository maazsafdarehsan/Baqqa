package org.Baqqaproject.Baqqa.client;

import org.Baqqaproject.Baqqa.api.client.MessageTracker;

import dagger.Module;
import dagger.Provides;

@Module
public class BaqqaClientModule {

	@Provides
	MessageTracker provideMessageTracker(MessageTrackerImpl messageTracker) {
		return messageTracker;
	}
}



