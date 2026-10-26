package org.Baqqaproject.bramble.account;

import org.Baqqaproject.bramble.api.account.AccountManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BaqqaAccountModule {

	@Provides
	@Singleton
	AccountManager provideAccountManager(BaqqaAccountManager accountManager) {
		return accountManager;
	}
}



