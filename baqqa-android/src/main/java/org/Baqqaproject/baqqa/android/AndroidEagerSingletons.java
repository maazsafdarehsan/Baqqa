package org.Baqqaproject.Baqqa.android;

interface AndroidEagerSingletons {

	void inject(AppModule.EagerSingletons init);

	class Helper {

		static void injectEagerSingletons(AndroidEagerSingletons c) {
			c.inject(new AppModule.EagerSingletons());
		}
	}
}



