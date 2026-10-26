package org.Baqqaproject.Baqqa.feed;

import org.Baqqaproject.bramble.BrambleCoreIntegrationTestEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.api.identity.IdentityManager;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.test.BrambleCoreIntegrationTestModule;
import org.Baqqaproject.bramble.test.TestDnsModule;
import org.Baqqaproject.bramble.test.TestPluginConfigModule;
import org.Baqqaproject.bramble.test.TestSocksModule;
import org.Baqqaproject.Baqqa.api.blog.BlogManager;
import org.Baqqaproject.Baqqa.api.feed.FeedManager;
import org.Baqqaproject.Baqqa.avatar.AvatarModule;
import org.Baqqaproject.Baqqa.blog.BlogModule;
import org.Baqqaproject.Baqqa.client.BaqqaClientModule;
import org.Baqqaproject.Baqqa.identity.IdentityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		BrambleCoreIntegrationTestModule.class,
		BrambleCoreModule.class,
		AvatarModule.class,
		BlogModule.class,
		BaqqaClientModule.class,
		FeedModule.class,
		IdentityModule.class,
		ModularMailboxModule.class,
		TestDnsModule.class,
		TestSocksModule.class,
		TestPluginConfigModule.class,
})
interface FeedManagerIntegrationTestComponent
		extends BrambleCoreIntegrationTestEagerSingletons {

	void inject(FeedManagerIntegrationTest testCase);

	void inject(AvatarModule.EagerSingletons init);

	void inject(BlogModule.EagerSingletons init);

	void inject(FeedModule.EagerSingletons init);

	IdentityManager getIdentityManager();

	LifecycleManager getLifecycleManager();

	FeedManager getFeedManager();

	BlogManager getBlogManager();

	class Helper {

		public static void injectEagerSingletons(
				FeedManagerIntegrationTestComponent c) {
			BrambleCoreIntegrationTestEagerSingletons.Helper
					.injectEagerSingletons(c);
			c.inject(new AvatarModule.EagerSingletons());
			c.inject(new BlogModule.EagerSingletons());
			c.inject(new FeedModule.EagerSingletons());
		}
	}
}



