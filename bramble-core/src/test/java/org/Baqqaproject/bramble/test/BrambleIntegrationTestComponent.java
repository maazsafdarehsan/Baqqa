package org.Baqqaproject.bramble.test;

import org.Baqqaproject.bramble.BrambleCoreIntegrationTestEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.connection.ConnectionManager;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.identity.IdentityManager;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;

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
public interface BrambleIntegrationTestComponent
		extends BrambleCoreIntegrationTestEagerSingletons {

	IdentityManager getIdentityManager();

	EventBus getEventBus();

	ConnectionManager getConnectionManager();

	ClientHelper getClientHelper();

}



