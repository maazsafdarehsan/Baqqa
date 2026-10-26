package org.Baqqaproject.bramble.system;

import org.Baqqaproject.bramble.api.system.Clock;

import dagger.Module;
import dagger.Provides;

@Module
public class ClockModule {

	@Provides
	Clock provideClock() {
		return new SystemClock();
	}
}



