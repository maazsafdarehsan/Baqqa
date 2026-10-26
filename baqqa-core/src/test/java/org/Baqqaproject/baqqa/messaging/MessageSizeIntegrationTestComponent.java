package org.Baqqaproject.Baqqa.messaging;

import org.Baqqaproject.bramble.BrambleCoreIntegrationTestEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.test.BrambleCoreIntegrationTestModule;
import org.Baqqaproject.bramble.test.TestDnsModule;
import org.Baqqaproject.bramble.test.TestPluginConfigModule;
import org.Baqqaproject.bramble.test.TestSocksModule;
import org.Baqqaproject.Baqqa.autodelete.AutoDeleteModule;
import org.Baqqaproject.Baqqa.avatar.AvatarModule;
import org.Baqqaproject.Baqqa.client.BaqqaClientModule;
import org.Baqqaproject.Baqqa.conversation.ConversationModule;
import org.Baqqaproject.Baqqa.forum.ForumModule;
import org.Baqqaproject.Baqqa.identity.IdentityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		BrambleCoreIntegrationTestModule.class,
		BrambleCoreModule.class,
		BaqqaClientModule.class,
		AutoDeleteModule.class,
		AvatarModule.class,
		ConversationModule.class,
		ForumModule.class,
		IdentityModule.class,
		MessagingModule.class,
		ModularMailboxModule.class,
		TestDnsModule.class,
		TestSocksModule.class,
		TestPluginConfigModule.class,
})
interface MessageSizeIntegrationTestComponent
		extends BrambleCoreIntegrationTestEagerSingletons {

	void inject(MessageSizeIntegrationTest testCase);

	void inject(AvatarModule.EagerSingletons init);

	void inject(ForumModule.EagerSingletons init);

	void inject(MessagingModule.EagerSingletons init);

	class Helper {

		public static void injectEagerSingletons(
				MessageSizeIntegrationTestComponent c) {
			BrambleCoreIntegrationTestEagerSingletons.Helper
					.injectEagerSingletons(c);
			c.inject(new AvatarModule.EagerSingletons());
			c.inject(new ForumModule.EagerSingletons());
			c.inject(new MessagingModule.EagerSingletons());
		}
	}
}



