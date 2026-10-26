package org.Baqqaproject.bramble.system;

import org.Baqqaproject.bramble.api.system.SecureRandomProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.Baqqaproject.bramble.util.OsUtils.isLinux;
import static org.Baqqaproject.bramble.util.OsUtils.isMac;

@Module
public class DesktopSecureRandomModule {

	@Provides
	@Singleton
	SecureRandomProvider provideSecureRandomProvider() {
		if (isLinux()) return new UnixSecureRandomProvider();
		return () -> null; // Use system default
	}
}



