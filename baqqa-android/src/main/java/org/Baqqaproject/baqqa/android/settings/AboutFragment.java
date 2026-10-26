package org.Baqqaproject.Baqqa.android.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.Baqqaproject.Baqqa.BuildConfig;
import org.Baqqaproject.Baqqa.R;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Intent.ACTION_VIEW;
import static java.util.logging.Logger.getLogger;
import static org.Baqqaproject.Baqqa.android.util.UiUtils.tryToStartActivity;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class AboutFragment extends Fragment {

	final static String TAG = AboutFragment.class.getName();
	private static final Logger LOG = getLogger(TAG);

	private TextView BaqqaVersion;
	private TextView torVersion;
	private TextView BaqqaWebsite;
	private TextView BaqqaSourceCode;
	private TextView BaqqaChangelog;
	private TextView BaqqaPrivacyPolicy;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_about, container,
				false);
	}

	@Override
	public void onStart() {
		super.onStart();
		requireActivity().setTitle(R.string.about_title);
		BaqqaVersion = requireActivity().findViewById(R.id.BaqqaVersion);
		BaqqaVersion.setText(
				getString(R.string.baqqa_version, BuildConfig.VERSION_NAME));
		torVersion = requireActivity().findViewById(R.id.TorVersion);
		torVersion.setText(
				getString(R.string.tor_version, BuildConfig.TorVersion));
		BaqqaWebsite = requireActivity().findViewById(R.id.BaqqaWebsite);
		BaqqaSourceCode = requireActivity().findViewById(R.id.BaqqaSourceCode);
		BaqqaChangelog = requireActivity().findViewById(R.id.BaqqaChangelog);
		BaqqaPrivacyPolicy =
				requireActivity().findViewById(R.id.BaqqaPrivacyPolicy);
		BaqqaWebsite.setOnClickListener(View -> {
			String url = "https://Baqqaproject.org/";
			goToUrl(url);
		});
		BaqqaSourceCode.setOnClickListener(View -> {
			String url = "https://code.Baqqaproject.org/Baqqa/Baqqa";
			goToUrl(url);
		});
		BaqqaChangelog.setOnClickListener(View -> {
			String url =
					"https://code.Baqqaproject.org/Baqqa/Baqqa/-/wikis/changelog";
			goToUrl(url);
		});
		BaqqaPrivacyPolicy.setOnClickListener(View -> {
			String url =
					"https://Baqqaproject.org/privacy-policy/";
			goToUrl(url);
		});
	}

	private void goToUrl(String url) {
		Intent i = new Intent(ACTION_VIEW);
		i.setData(Uri.parse(url));
		tryToStartActivity(requireActivity(), i);
	}

}



