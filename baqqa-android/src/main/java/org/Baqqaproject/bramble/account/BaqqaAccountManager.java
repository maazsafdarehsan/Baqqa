package org.Baqqaproject.bramble.account;

import android.app.Application;
import android.content.SharedPreferences;

import org.Baqqaproject.bramble.api.crypto.CryptoComponent;
import org.Baqqaproject.bramble.api.db.DatabaseConfig;
import org.Baqqaproject.bramble.api.identity.IdentityManager;
import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.Localizer;
import org.Baqqaproject.Baqqa.android.util.UiUtils;

import javax.inject.Inject;

class BaqqaAccountManager extends AndroidAccountManager {

	@Inject
	BaqqaAccountManager(DatabaseConfig databaseConfig, CryptoComponent crypto,
			IdentityManager identityManager, SharedPreferences prefs,
			Application app) {
		super(databaseConfig, crypto, identityManager, prefs, app);
	}

	@Override
	public void deleteAccount() {
		synchronized (stateChangeLock) {
			super.deleteAccount();
			Localizer.reinitialize();
			UiUtils.setTheme(appContext,
					appContext.getString(R.string.pref_theme_system_value));
		}
	}
}



