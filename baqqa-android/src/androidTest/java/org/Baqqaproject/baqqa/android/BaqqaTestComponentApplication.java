package org.Baqqaproject.Baqqa.android;

import org.Baqqaproject.bramble.BrambleAndroidEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreEagerSingletons;
import org.Baqqaproject.Baqqa.BaqqaCoreEagerSingletons;

public class BaqqaTestComponentApplication extends BaqqaApplicationImpl {

	@Override
	protected AndroidComponent createApplicationComponent() {
		AndroidComponent component = DaggerBaqqaUiTestComponent.builder()
				.appModule(new AppModule(this)).build();
		// We need to load the eager singletons directly after making the
		// dependency graphs
		BrambleCoreEagerSingletons.Helper.injectEagerSingletons(component);
		BrambleAndroidEagerSingletons.Helper.injectEagerSingletons(component);
		BaqqaCoreEagerSingletons.Helper.injectEagerSingletons(component);
		AndroidEagerSingletons.Helper.injectEagerSingletons(component);
		return component;
	}

	@Override
	public boolean isInstrumentationTest() {
		return true;
	}

}



