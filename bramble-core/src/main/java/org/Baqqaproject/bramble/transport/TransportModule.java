package org.Baqqaproject.bramble.transport;

import org.Baqqaproject.bramble.api.crypto.StreamDecrypterFactory;
import org.Baqqaproject.bramble.api.crypto.StreamEncrypterFactory;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.transport.KeyManager;
import org.Baqqaproject.bramble.api.transport.StreamReaderFactory;
import org.Baqqaproject.bramble.api.transport.StreamWriterFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TransportModule {

	public static class EagerSingletons {
		@Inject
		KeyManager keyManager;
	}

	@Provides
	StreamReaderFactory provideStreamReaderFactory(
			StreamDecrypterFactory streamDecrypterFactory) {
		return new StreamReaderFactoryImpl(streamDecrypterFactory);
	}

	@Provides
	StreamWriterFactory provideStreamWriterFactory(
			StreamEncrypterFactory streamEncrypterFactory) {
		return new StreamWriterFactoryImpl(streamEncrypterFactory);
	}

	@Provides
	TransportKeyManagerFactory provideTransportKeyManagerFactory(
			TransportKeyManagerFactoryImpl transportKeyManagerFactory) {
		return transportKeyManagerFactory;
	}

	@Provides
	@Singleton
	KeyManager provideKeyManager(LifecycleManager lifecycleManager,
			EventBus eventBus, KeyManagerImpl keyManager) {
		lifecycleManager.registerService(keyManager);
		eventBus.addListener(keyManager);
		return keyManager;
	}
}



