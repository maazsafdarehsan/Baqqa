package org.Baqqaproject.Baqqa.messaging;

import org.Baqqaproject.bramble.BrambleCoreIntegrationTestEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.api.connection.ConnectionManager;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.identity.IdentityManager;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.test.BrambleCoreIntegrationTestModule;
import org.Baqqaproject.bramble.test.TestDnsModule;
import org.Baqqaproject.bramble.test.TestPluginConfigModule;
import org.Baqqaproject.bramble.test.TestSocksModule;
import org.Baqqaproject.Baqqa.api.messaging.MessagingManager;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFactory;
import org.Baqqaproject.Baqqa.autodelete.AutoDeleteModule;
import org.Baqqaproject.Baqqa.client.BaqqaClientModule;
import org.Baqqaproject.Baqqa.conversation.ConversationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		AutoDeleteModule.class,
		BrambleCoreIntegrationTestModule.class,
		BrambleCoreModule.class,
		BaqqaClientModule.class,
		ConversationModule.class,
		MessagingModule.class,
		ModularMailboxModule.class,
		TestDnsModule.class,
		TestSocksModule.class,
		TestPluginConfigModule.class,
})
interface SimplexMessagingIntegrationTestComponent
		extends BrambleCoreIntegrationTestEagerSingletons {

	void inject(MessagingModule.EagerSingletons init);

	LifecycleManager getLifecycleManager();

	IdentityManager getIdentityManager();

	ContactManager getContactManager();

	MessagingManager getMessagingManager();

	PrivateMessageFactory getPrivateMessageFactory();

	EventBus getEventBus();

	ConnectionManager getConnectionManager();

	class Helper {

		public static void injectEagerSingletons(
				SimplexMessagingIntegrationTestComponent c) {
			BrambleCoreIntegrationTestEagerSingletons.Helper
					.injectEagerSingletons(c);
			c.inject(new MessagingModule.EagerSingletons());
		}
	}
}



