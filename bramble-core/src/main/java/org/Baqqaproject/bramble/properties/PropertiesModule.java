package org.Baqqaproject.bramble.properties;

import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.data.MetadataEncoder;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.properties.TransportPropertyManager;
import org.Baqqaproject.bramble.api.sync.validation.ValidationManager;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.bramble.api.versioning.ClientVersioningManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.Baqqaproject.bramble.api.properties.TransportPropertyManager.CLIENT_ID;
import static org.Baqqaproject.bramble.api.properties.TransportPropertyManager.MAJOR_VERSION;
import static org.Baqqaproject.bramble.api.properties.TransportPropertyManager.MINOR_VERSION;

@Module
public class PropertiesModule {

	public static class EagerSingletons {
		@Inject
		TransportPropertyValidator transportPropertyValidator;
		@Inject
		TransportPropertyManager transportPropertyManager;
	}

	@Provides
	@Singleton
	TransportPropertyValidator getValidator(ValidationManager validationManager,
			ClientHelper clientHelper, MetadataEncoder metadataEncoder,
			Clock clock) {
		TransportPropertyValidator validator = new TransportPropertyValidator(
				clientHelper, metadataEncoder, clock);
		validationManager.registerMessageValidator(CLIENT_ID, MAJOR_VERSION,
				validator);
		return validator;
	}

	@Provides
	@Singleton
	TransportPropertyManager getTransportPropertyManager(
			LifecycleManager lifecycleManager,
			ValidationManager validationManager, ContactManager contactManager,
			ClientVersioningManager clientVersioningManager,
			TransportPropertyManagerImpl transportPropertyManager) {
		lifecycleManager.registerOpenDatabaseHook(transportPropertyManager);
		validationManager.registerIncomingMessageHook(CLIENT_ID, MAJOR_VERSION,
				transportPropertyManager);
		contactManager.registerContactHook(transportPropertyManager);
		clientVersioningManager.registerClient(CLIENT_ID, MAJOR_VERSION,
				MINOR_VERSION, transportPropertyManager);
		return transportPropertyManager;
	}
}



