package org.Baqqaproject.bramble.plugin.file;

import org.Baqqaproject.bramble.BrambleCoreEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.identity.IdentityManager;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.plugin.file.RemovableDriveManager;
import org.Baqqaproject.bramble.battery.DefaultBatteryManagerModule;
import org.Baqqaproject.bramble.event.DefaultEventExecutorModule;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.system.DefaultThreadFactoryModule;
import org.Baqqaproject.bramble.system.DefaultWakefulIoExecutorModule;
import org.Baqqaproject.bramble.system.TimeTravelModule;
import org.Baqqaproject.bramble.test.TestDatabaseConfigModule;
import org.Baqqaproject.bramble.test.TestDnsModule;
import org.Baqqaproject.bramble.test.TestFeatureFlagModule;
import org.Baqqaproject.bramble.test.TestMailboxDirectoryModule;
import org.Baqqaproject.bramble.test.TestSecureRandomModule;
import org.Baqqaproject.bramble.test.TestSocksModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		BrambleCoreModule.class,
		DefaultBatteryManagerModule.class,
		DefaultEventExecutorModule.class,
		DefaultWakefulIoExecutorModule.class,
		DefaultThreadFactoryModule.class,
		TestDatabaseConfigModule.class,
		TestDnsModule.class,
		TestFeatureFlagModule.class,
		TestMailboxDirectoryModule.class,
		RemovableDriveIntegrationTestModule.class,
		RemovableDriveModule.class,
		ModularMailboxModule.class,
		TestSecureRandomModule.class,
		TimeTravelModule.class,
		TestSocksModule.class,
})
interface RemovableDriveIntegrationTestComponent
		extends BrambleCoreEagerSingletons {

	ContactManager getContactManager();

	EventBus getEventBus();

	IdentityManager getIdentityManager();

	LifecycleManager getLifecycleManager();

	RemovableDriveManager getRemovableDriveManager();

	class Helper {

		public static void injectEagerSingletons(
				RemovableDriveIntegrationTestComponent c) {
			BrambleCoreEagerSingletons.Helper.injectEagerSingletons(c);
		}
	}
}



