package org.Baqqaproject.bramble.plugin.tor;

import org.Baqqaproject.bramble.api.battery.BatteryManager;
import org.Baqqaproject.bramble.api.crypto.CryptoComponent;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.event.EventExecutor;
import org.Baqqaproject.bramble.api.lifecycle.IoExecutor;
import org.Baqqaproject.bramble.api.network.NetworkManager;
import org.Baqqaproject.bramble.api.plugin.Backoff;
import org.Baqqaproject.bramble.api.plugin.BackoffFactory;
import org.Baqqaproject.bramble.api.plugin.PluginCallback;
import org.Baqqaproject.bramble.api.plugin.TorControlPort;
import org.Baqqaproject.bramble.api.plugin.TorDirectory;
import org.Baqqaproject.bramble.api.plugin.TorSocksPort;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.bramble.api.system.WakefulIoExecutor;
import org.briarproject.nullsafety.NotNullByDefault;
import org.briarproject.onionwrapper.CircumventionProvider;
import org.briarproject.onionwrapper.LocationUtils;
import org.briarproject.onionwrapper.TorWrapper;
import org.briarproject.onionwrapper.WindowsTorWrapper;

import java.io.File;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.net.SocketFactory;

import static java.util.logging.Level.INFO;
import static org.Baqqaproject.bramble.util.OsUtils.isWindows;

@Immutable
@NotNullByDefault
public class WindowsTorPluginFactory extends TorPluginFactory {

	@Inject
	WindowsTorPluginFactory(@IoExecutor Executor ioExecutor,
			@EventExecutor Executor eventExecutor,
			@WakefulIoExecutor Executor wakefulIoExecutor,
			NetworkManager networkManager,
			LocationUtils locationUtils,
			EventBus eventBus,
			SocketFactory torSocketFactory,
			BackoffFactory backoffFactory,
			CircumventionProvider circumventionProvider,
			BatteryManager batteryManager,
			Clock clock,
			CryptoComponent crypto,
			@TorDirectory File torDirectory,
			@TorSocksPort int torSocksPort,
			@TorControlPort int torControlPort) {
		super(ioExecutor, eventExecutor, wakefulIoExecutor, networkManager,
				locationUtils, eventBus, torSocketFactory, backoffFactory,
				circumventionProvider, batteryManager, clock, crypto,
				torDirectory, torSocksPort, torControlPort);
	}

	@Nullable
	@Override
	String getArchitectureForTorBinary() {
		if (!isWindows()) return null;
		String arch = System.getProperty("os.arch");
		if (LOG.isLoggable(INFO)) {
			LOG.info("System's os.arch is " + arch);
		}
		if (arch.equals("amd64")) return "x86_64";
		return null;
	}

	@Override
	TorPlugin createPluginInstance(Backoff backoff,
			TorRendezvousCrypto torRendezvousCrypto, PluginCallback callback,
			String architecture) {
		TorWrapper tor = new WindowsTorWrapper(ioExecutor, eventExecutor,
				architecture, torDirectory, torSocksPort, torControlPort);
		return new TorPlugin(ioExecutor, wakefulIoExecutor, networkManager,
				locationUtils, torSocketFactory, circumventionProvider,
				batteryManager, backoff, torRendezvousCrypto, tor, callback,
				MAX_LATENCY, MAX_IDLE_TIME);
	}
}




