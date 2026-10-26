package org.Baqqaproject.Baqqa.android.logging;

import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.system.ClockModule;
import org.Baqqaproject.bramble.test.TestSecureRandomModule;

import java.security.SecureRandom;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		ClockModule.class,
		BrambleCoreModule.class,
		TestSecureRandomModule.class,
		LoggingModule.class,
		LoggingTestModule.class,
})
public interface LoggingComponent {

	SecureRandom random();

	CachingLogHandler cachingLogHandler();

	LogEncrypter logEncrypter();

	LogDecrypter logDecrypter();

}



