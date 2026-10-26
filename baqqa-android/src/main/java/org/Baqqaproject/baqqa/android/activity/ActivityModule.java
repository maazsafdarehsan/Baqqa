package org.Baqqaproject.Baqqa.android.activity;

import android.app.Activity;

import org.Baqqaproject.Baqqa.android.controller.BaqqaController;
import org.Baqqaproject.Baqqa.android.controller.BaqqaControllerImpl;
import org.Baqqaproject.Baqqa.android.controller.DbController;
import org.Baqqaproject.Baqqa.android.controller.DbControllerImpl;

import dagger.Module;
import dagger.Provides;

import static org.Baqqaproject.Baqqa.android.BaqqaService.BaqqaServiceConnection;

@Module
public class ActivityModule {

	private final BaseActivity activity;

	public ActivityModule(BaseActivity activity) {
		this.activity = activity;
	}

	@ActivityScope
	@Provides
	BaseActivity provideBaseActivity() {
		return activity;
	}

	@ActivityScope
	@Provides
	Activity provideActivity() {
		return activity;
	}

	@ActivityScope
	@Provides
	protected BaqqaController provideBaqqaController(
			BaqqaControllerImpl BaqqaController) {
		activity.addLifecycleController(BaqqaController);
		return BaqqaController;
	}

	@ActivityScope
	@Provides
	DbController provideDBController(DbControllerImpl dbController) {
		return dbController;
	}

	@ActivityScope
	@Provides
	BaqqaServiceConnection provideBaqqaServiceConnection() {
		return new BaqqaServiceConnection();
	}
}



