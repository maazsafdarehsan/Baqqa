package org.Baqqaproject.bramble;

import org.Baqqaproject.bramble.battery.AndroidBatteryModule;
import org.Baqqaproject.bramble.io.DnsModule;
import org.Baqqaproject.bramble.network.AndroidNetworkModule;
import org.Baqqaproject.bramble.plugin.tor.CircumventionModule;
import org.Baqqaproject.bramble.reporting.ReportingModule;
import org.Baqqaproject.bramble.socks.SocksModule;
import org.Baqqaproject.bramble.system.AndroidSystemModule;
import org.Baqqaproject.bramble.system.AndroidTaskSchedulerModule;
import org.Baqqaproject.bramble.system.AndroidWakeLockModule;
import org.Baqqaproject.bramble.system.AndroidWakefulIoExecutorModule;
import org.Baqqaproject.bramble.system.DefaultThreadFactoryModule;

import dagger.Module;

@Module(includes = {
		AndroidBatteryModule.class,
		AndroidNetworkModule.class,
		AndroidSystemModule.class,
		AndroidTaskSchedulerModule.class,
		AndroidWakefulIoExecutorModule.class,
		AndroidWakeLockModule.class,
		DefaultThreadFactoryModule.class,
		CircumventionModule.class,
		DnsModule.class,
		ReportingModule.class,
		SocksModule.class
})
public class BrambleAndroidModule {
}



