package org.Baqqaproject.bramble.settings;

import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.settings.SettingsManager;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {

	@Provides
	SettingsManager provideSettingsManager(DatabaseComponent db) {
		return new SettingsManagerImpl(db);
	}

}



