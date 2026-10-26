package org.Baqqaproject.Baqqa.android.privategroup.creation;

import org.Baqqaproject.Baqqa.android.activity.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class CreateGroupModule {

	@ActivityScope
	@Provides
	CreateGroupController provideCreateGroupController(
			CreateGroupControllerImpl createGroupController) {
		return createGroupController;
	}

}



