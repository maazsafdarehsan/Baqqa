package org.Baqqaproject.bramble.test;

import org.Baqqaproject.bramble.battery.DefaultBatteryManagerModule;
import org.Baqqaproject.bramble.event.DefaultEventExecutorModule;
import org.Baqqaproject.bramble.system.DefaultWakefulIoExecutorModule;
import org.Baqqaproject.bramble.system.TimeTravelModule;

import dagger.Module;

@Module(includes = {
		DefaultBatteryManagerModule.class,
		DefaultEventExecutorModule.class,
		DefaultWakefulIoExecutorModule.class,
		TestThreadFactoryModule.class,
		TestDatabaseConfigModule.class,
		TestFeatureFlagModule.class,
		TestMailboxDirectoryModule.class,
		TestSecureRandomModule.class,
		TimeTravelModule.class
})
public class BrambleCoreIntegrationTestModule {

}



