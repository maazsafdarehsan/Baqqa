package org.Baqqaproject.Baqqa.android.blog;

import android.os.Bundle;

import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.activity.ActivityComponent;
import org.Baqqaproject.Baqqa.android.activity.BaqqaActivity;
import org.Baqqaproject.Baqqa.android.blog.RssImportResult.FileImportError;
import org.Baqqaproject.Baqqa.android.blog.RssImportResult.FileImportSuccess;
import org.Baqqaproject.Baqqa.android.blog.RssImportResult.UrlImportError;
import org.Baqqaproject.Baqqa.android.blog.RssImportResult.UrlImportSuccess;
import org.Baqqaproject.Baqqa.android.fragment.BaseFragment.BaseFragmentListener;
import org.Baqqaproject.Baqqa.android.fragment.ErrorFragment;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static org.Baqqaproject.Baqqa.android.util.UiUtils.showFragment;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class RssFeedActivity extends BaqqaActivity
		implements BaseFragmentListener {

	@Inject
	ViewModelProvider.Factory viewModelFactory;
	private RssFeedViewModel viewModel;

	@Override
	public void injectActivity(ActivityComponent component) {
		component.inject(this);

		viewModel = new ViewModelProvider(this, viewModelFactory)
				.get(RssFeedViewModel.class);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fragment_container);

		if (savedInstanceState == null) {
			showInitialFragment(RssFeedManageFragment.newInstance());
		}

		viewModel.getImportResult().observeEvent(this, this::onImportResult);
	}

	private void onImportResult(@Nullable RssImportResult result) {
		FragmentManager fm = getSupportFragmentManager();
		if (result instanceof UrlImportSuccess) {
			if (fm.findFragmentByTag(RssFeedImportFragment.TAG) != null) {
				onBackPressed();
			}
		} else if (result instanceof UrlImportError) {
			String url = ((UrlImportError) result).url;
			RssFeedImportFailedDialogFragment dialog =
					RssFeedImportFailedDialogFragment.newInstance(url);
			dialog.show(fm, RssFeedImportFailedDialogFragment.TAG);
		} else if (result instanceof FileImportSuccess) {
			// pop stack back to before the initial import fragment
			fm.popBackStackImmediate(RssFeedImportFragment.TAG,
					POP_BACK_STACK_INCLUSIVE);
		} else if (result instanceof FileImportError) {
			// pop stack back to initial import fragment
			fm.popBackStackImmediate(RssFeedImportFragment.TAG, 0);
			// show error fragment
			Fragment f = ErrorFragment.newInstance(
					getString(R.string.blogs_rss_feeds_import_error));
			String tag = ErrorFragment.TAG;
			showFragment(fm, f, tag);
		}
	}
}




