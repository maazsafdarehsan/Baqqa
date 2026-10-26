package org.Baqqaproject.Baqqa.android.privategroup.reveal;

import org.Baqqaproject.Baqqa.android.activity.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class GroupRevealModule {

	@ActivityScope
	@Provides
	RevealContactsController provideRevealContactsController(
			RevealContactsControllerImpl revealContactsController) {
		return revealContactsController;
	}
}



