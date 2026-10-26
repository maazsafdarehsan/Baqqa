package org.Baqqaproject.bramble;

import org.Baqqaproject.bramble.io.DnsModule;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.network.JavaNetworkModule;
import org.Baqqaproject.bramble.plugin.tor.CircumventionModule;
import org.Baqqaproject.bramble.socks.SocksModule;
import org.Baqqaproject.bramble.system.JavaSystemModule;

import dagger.Module;

@Module(includes = {
		CircumventionModule.class,
		DnsModule.class,
		JavaNetworkModule.class,
		JavaSystemModule.class,
		ModularMailboxModule.class,
		SocksModule.class
})
public class BrambleJavaModule {

}



