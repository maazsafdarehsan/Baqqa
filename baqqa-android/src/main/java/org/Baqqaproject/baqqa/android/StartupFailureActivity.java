package org.Baqqaproject.Baqqa.android;

import android.content.Intent;
import android.os.Bundle;

import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.activity.ActivityComponent;
import org.Baqqaproject.Baqqa.android.activity.BaseActivity;
import org.Baqqaproject.Baqqa.android.fragment.BaseFragment.BaseFragmentListener;
import org.Baqqaproject.Baqqa.android.fragment.ErrorFragment;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static org.Baqqaproject.bramble.api.lifecycle.LifecycleManager.StartResult;
import static org.Baqqaproject.Baqqa.android.BaqqaService.EXTRA_START_RESULT;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class StartupFailureActivity extends BaseActivity implements
		BaseFragmentListener {

	@Override
	public void onCreate(@Nullable Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.activity_fragment_container);
		handleIntent(getIntent());
	}

	@Override
	public void injectActivity(ActivityComponent component) {
		component.inject(this);
	}

	private void handleIntent(Intent i) {
		StartResult result =
				(StartResult) i.getSerializableExtra(EXTRA_START_RESULT);

		// show proper error message
		int errorRes;
		switch (result) {
			case CLOCK_ERROR:
				errorRes = R.string.startup_failed_clock_error;
				break;
			case DATA_TOO_OLD_ERROR:
				errorRes = R.string.startup_failed_data_too_old_error;
				break;
			case DATA_TOO_NEW_ERROR:
				errorRes = R.string.startup_failed_data_too_new_error;
				break;
			case DB_ERROR:
				errorRes = R.string.startup_failed_db_error;
				break;
			case SERVICE_ERROR:
				errorRes = R.string.startup_failed_service_error;
				break;
			default:
				throw new IllegalArgumentException();
		}
		showInitialFragment(ErrorFragment.newInstance(getString(errorRes)));
	}

	@Override
	public void runOnDbThread(@NonNull Runnable runnable) {
		throw new UnsupportedOperationException();
	}
}




