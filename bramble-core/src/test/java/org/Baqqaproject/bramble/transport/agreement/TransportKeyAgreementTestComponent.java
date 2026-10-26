package org.Baqqaproject.bramble.transport.agreement;

import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.api.client.ContactGroupFactory;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.properties.TransportPropertyManager;
import org.Baqqaproject.bramble.api.transport.KeyManager;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.test.BrambleCoreIntegrationTestModule;
import org.Baqqaproject.bramble.test.BrambleIntegrationTestComponent;
import org.Baqqaproject.bramble.test.TestDnsModule;
import org.Baqqaproject.bramble.test.TestPluginConfigModule;
import org.Baqqaproject.bramble.test.TestSocksModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		BrambleCoreIntegrationTestModule.class,
		BrambleCoreModule.class,
		ModularMailboxModule.class,
		TestDnsModule.class,
		TestSocksModule.class,
		TestPluginConfigModule.class,
})
interface TransportKeyAgreementTestComponent
		extends BrambleIntegrationTestComponent {

	KeyManager getKeyManager();

	TransportKeyAgreementManagerImpl getTransportKeyAgreementManager();

	ContactManager getContactManager();

	LifecycleManager getLifecycleManager();

	ContactGroupFactory getContactGroupFactory();

	SessionParser getSessionParser();

	TransportPropertyManager getTransportPropertyManager();

	DatabaseComponent getDatabaseComponent();
}



