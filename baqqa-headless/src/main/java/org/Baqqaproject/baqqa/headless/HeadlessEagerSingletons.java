package org.Baqqaproject.Baqqa.headless;

import org.Baqqaproject.bramble.system.DefaultTaskSchedulerModule;

public interface HeadlessEagerSingletons {

	void inject(DefaultTaskSchedulerModule.EagerSingletons init);

	class Helper {

		public static void injectEagerSingletons(HeadlessEagerSingletons c) {
			c.inject(new DefaultTaskSchedulerModule.EagerSingletons());
		}
	}
}



