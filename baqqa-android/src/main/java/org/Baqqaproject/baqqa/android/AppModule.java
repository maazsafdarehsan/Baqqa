package org.Baqqaproject.Baqqa.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.vanniktech.emoji.RecentEmoji;

import org.Baqqaproject.bramble.api.FeatureFlags;
import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.crypto.CryptoComponent;
import org.Baqqaproject.bramble.api.crypto.KeyStrengthener;
import org.Baqqaproject.bramble.api.crypto.PublicKey;
import org.Baqqaproject.bramble.api.db.DatabaseConfig;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.mailbox.MailboxDirectory;
import org.Baqqaproject.bramble.api.plugin.BluetoothConstants;
import org.Baqqaproject.bramble.api.plugin.LanTcpConstants;
import org.Baqqaproject.bramble.api.plugin.PluginConfig;
import org.Baqqaproject.bramble.api.plugin.TorControlPort;
import org.Baqqaproject.bramble.api.plugin.TorDirectory;
import org.Baqqaproject.bramble.api.plugin.TorSocksPort;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.Baqqaproject.bramble.api.plugin.duplex.DuplexPluginFactory;
import org.Baqqaproject.bramble.api.plugin.simplex.SimplexPluginFactory;
import org.Baqqaproject.bramble.api.reporting.DevConfig;
import org.Baqqaproject.bramble.plugin.bluetooth.AndroidBluetoothPluginFactory;
import org.Baqqaproject.bramble.plugin.file.AndroidRemovableDrivePluginFactory;
import org.Baqqaproject.bramble.plugin.file.MailboxPluginFactory;
import org.Baqqaproject.bramble.plugin.tcp.AndroidLanTcpPluginFactory;
import org.Baqqaproject.bramble.plugin.tor.AndroidTorPluginFactory;
import org.Baqqaproject.bramble.util.AndroidUtils;
import org.Baqqaproject.bramble.util.StringUtils;
import org.Baqqaproject.Baqqa.android.account.DozeHelperModule;
import org.Baqqaproject.Baqqa.android.account.LockManagerImpl;
import org.Baqqaproject.Baqqa.android.account.SetupModule;
import org.Baqqaproject.Baqqa.android.blog.BlogModule;
import org.Baqqaproject.Baqqa.android.contact.ContactListModule;
import org.Baqqaproject.Baqqa.android.contact.add.nearby.AddNearbyContactModule;
import org.Baqqaproject.Baqqa.android.contact.connect.ConnectViaBluetoothModule;
import org.Baqqaproject.Baqqa.android.forum.ForumModule;
import org.Baqqaproject.Baqqa.android.hotspot.HotspotModule;
import org.Baqqaproject.Baqqa.android.introduction.IntroductionModule;
import org.Baqqaproject.Baqqa.android.logging.LoggingModule;
import org.Baqqaproject.Baqqa.android.login.LoginModule;
import org.Baqqaproject.Baqqa.android.mailbox.MailboxModule;
import org.Baqqaproject.Baqqa.android.navdrawer.NavDrawerModule;
import org.Baqqaproject.Baqqa.android.privategroup.conversation.GroupConversationModule;
import org.Baqqaproject.Baqqa.android.privategroup.list.GroupListModule;
import org.Baqqaproject.Baqqa.android.removabledrive.TransferDataModule;
import org.Baqqaproject.Baqqa.android.reporting.DevReportModule;
import org.Baqqaproject.Baqqa.android.settings.SettingsModule;
import org.Baqqaproject.Baqqa.android.sharing.SharingModule;
import org.Baqqaproject.Baqqa.android.test.TestAvatarCreatorImpl;
import org.Baqqaproject.Baqqa.android.viewmodel.ViewModelModule;
import org.Baqqaproject.Baqqa.api.android.AndroidNotificationManager;
import org.Baqqaproject.Baqqa.api.android.DozeWatchdog;
import org.Baqqaproject.Baqqa.api.android.LockManager;
import org.Baqqaproject.Baqqa.api.android.NetworkUsageMetrics;
import org.Baqqaproject.Baqqa.api.android.ScreenFilterMonitor;
import org.Baqqaproject.Baqqa.api.test.TestAvatarCreator;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.Baqqaproject.bramble.api.plugin.TorConstants.DEFAULT_CONTROL_PORT;
import static org.Baqqaproject.bramble.api.plugin.TorConstants.DEFAULT_SOCKS_PORT;
import static org.Baqqaproject.bramble.api.reporting.ReportingConstants.DEV_ONION_ADDRESS;
import static org.Baqqaproject.bramble.api.reporting.ReportingConstants.DEV_PUBLIC_KEY_HEX;
import static org.Baqqaproject.Baqqa.android.TestingConstants.IS_DEBUG_BUILD;

@Module(includes = {
		SetupModule.class,
		DozeHelperModule.class,
		AddNearbyContactModule.class,
		LoggingModule.class,
		LoginModule.class,
		NavDrawerModule.class,
		ViewModelModule.class,
		SettingsModule.class,
		DevReportModule.class,
		ContactListModule.class,
		IntroductionModule.class,
		ConnectViaBluetoothModule.class,
		// below need to be within same scope as ViewModelProvider.Factory
		BlogModule.class,
		ForumModule.class,
		GroupListModule.class,
		GroupConversationModule.class,
		SharingModule.class,
		HotspotModule.class,
		TransferDataModule.class,
		MailboxModule.class,
})
public class AppModule {

	static class EagerSingletons {
		@Inject
		AndroidNotificationManager androidNotificationManager;
		@Inject
		ScreenFilterMonitor screenFilterMonitor;
		@Inject
		NetworkUsageMetrics networkUsageMetrics;
		@Inject
		DozeWatchdog dozeWatchdog;
		@Inject
		LockManager lockManager;
		@Inject
		RecentEmoji recentEmoji;
	}

	private final Application application;

	public AppModule(Application application) {
		this.application = application;
	}

	public static AndroidComponent getAndroidComponent(Context ctx) {
		BaqqaApplication app = (BaqqaApplication) ctx.getApplicationContext();
		return app.getApplicationComponent();
	}

	@Provides
	@Singleton
	Application providesApplication() {
		return application;
	}

	@Provides
	@Singleton
	DatabaseConfig provideDatabaseConfig(Application app) {
		//FIXME: StrictMode
		StrictMode.ThreadPolicy tp = StrictMode.allowThreadDiskReads();
		StrictMode.allowThreadDiskWrites();
		File dbDir = app.getApplicationContext().getDir("db", MODE_PRIVATE);
		File keyDir = app.getApplicationContext().getDir("key", MODE_PRIVATE);
		StrictMode.setThreadPolicy(tp);
		KeyStrengthener keyStrengthener = SDK_INT >= 23
				? new AndroidKeyStrengthener() : null;
		return new AndroidDatabaseConfig(dbDir, keyDir, keyStrengthener);
	}

	@Provides
	@Singleton
	@MailboxDirectory
	File provideMailboxDirectory(Application app) {
		return app.getDir("mailbox", MODE_PRIVATE);
	}

	@Provides
	@Singleton
	@TorDirectory
	File provideTorDirectory(Application app) {
		return app.getDir("tor", MODE_PRIVATE);
	}

	@Provides
	@Singleton
	@TorSocksPort
	int provideTorSocksPort() {
		if (!IS_DEBUG_BUILD) {
			return DEFAULT_SOCKS_PORT;
		} else {
			return DEFAULT_SOCKS_PORT + 2;
		}
	}

	@Provides
	@Singleton
	@TorControlPort
	int provideTorControlPort() {
		if (!IS_DEBUG_BUILD) {
			return DEFAULT_CONTROL_PORT;
		} else {
			return DEFAULT_CONTROL_PORT + 2;
		}
	}

	@Provides
	@Singleton
	PluginConfig providePluginConfig(AndroidBluetoothPluginFactory bluetooth,
			AndroidTorPluginFactory tor, AndroidLanTcpPluginFactory lan,
			AndroidRemovableDrivePluginFactory drive,
			MailboxPluginFactory mailbox, FeatureFlags featureFlags) {
		@NotNullByDefault
		PluginConfig pluginConfig = new PluginConfig() {

			@Override
			public Collection<DuplexPluginFactory> getDuplexFactories() {
				return asList(bluetooth, tor, lan);
			}

			@Override
			public Collection<SimplexPluginFactory> getSimplexFactories() {
				List<SimplexPluginFactory> simplex = new ArrayList<>();
				simplex.add(mailbox);
				simplex.add(drive);
				return simplex;
			}

			@Override
			public boolean shouldPoll() {
				return true;
			}

			@Override
			public Map<TransportId, List<TransportId>> getTransportPreferences() {
				// Prefer LAN to Bluetooth
				return singletonMap(BluetoothConstants.ID,
						singletonList(LanTcpConstants.ID));
			}
		};
		return pluginConfig;
	}

	@Provides
	@Singleton
	DevConfig provideDevConfig(Application app, CryptoComponent crypto) {
		@NotNullByDefault
		DevConfig devConfig = new DevConfig() {

			@Override
			public PublicKey getDevPublicKey() {
				try {
					return crypto.getMessageKeyParser().parsePublicKey(
							StringUtils.fromHexString(DEV_PUBLIC_KEY_HEX));
				} catch (GeneralSecurityException | FormatException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public String getDevOnionAddress() {
				return DEV_ONION_ADDRESS;
			}

			@Override
			public File getReportDir() {
				return AndroidUtils.getReportDir(app.getApplicationContext());
			}

			@Override
			public File getLogcatFile() {
				return AndroidUtils.getLogcatFile(app.getApplicationContext());
			}
		};
		return devConfig;
	}

	@Provides
	TestAvatarCreator provideTestAvatarCreator(
			TestAvatarCreatorImpl testAvatarCreator) {
		return testAvatarCreator;
	}

	@Provides
	SharedPreferences provideSharedPreferences(Application app) {
		// FIXME unify this with getDefaultSharedPreferences()
		return app.getSharedPreferences("db", MODE_PRIVATE);
	}

	@Provides
	@Singleton
	AndroidNotificationManager provideAndroidNotificationManager(
			LifecycleManager lifecycleManager, EventBus eventBus,
			AndroidNotificationManagerImpl notificationManager) {
		lifecycleManager.registerService(notificationManager);
		eventBus.addListener(notificationManager);
		return notificationManager;
	}

	@Provides
	@Singleton
	ScreenFilterMonitor provideScreenFilterMonitor(
			LifecycleManager lifecycleManager,
			ScreenFilterMonitorImpl screenFilterMonitor) {
		if (SDK_INT <= 29) {
			// this keeps track of installed apps and does not work on API 30+
			lifecycleManager.registerService(screenFilterMonitor);
		}
		return screenFilterMonitor;
	}

	@Provides
	@Singleton
	NetworkUsageMetrics provideNetworkUsageMetrics(
			LifecycleManager lifecycleManager) {
		NetworkUsageMetrics networkUsageMetrics = new NetworkUsageMetricsImpl();
		lifecycleManager.registerService(networkUsageMetrics);
		return networkUsageMetrics;
	}

	@Provides
	@Singleton
	DozeWatchdog provideDozeWatchdog(LifecycleManager lifecycleManager) {
		DozeWatchdogImpl dozeWatchdog = new DozeWatchdogImpl(application);
		lifecycleManager.registerService(dozeWatchdog);
		return dozeWatchdog;
	}

	@Provides
	@Singleton
	LockManager provideLockManager(LifecycleManager lifecycleManager,
			EventBus eventBus, LockManagerImpl lockManager) {
		lifecycleManager.registerService(lockManager);
		eventBus.addListener(lockManager);
		return lockManager;
	}

	@Provides
	@Singleton
	RecentEmoji provideRecentEmoji(LifecycleManager lifecycleManager,
			RecentEmojiImpl recentEmoji) {
		lifecycleManager.registerOpenDatabaseHook(recentEmoji);
		return recentEmoji;
	}

	@Provides
	FeatureFlags provideFeatureFlags() {
		return new FeatureFlags() {

			@Override
			public boolean shouldEnableImageAttachments() {
				return true;
			}

			@Override
			public boolean shouldEnableProfilePictures() {
				return true;
			}

			@Override
			public boolean shouldEnableDisappearingMessages() {
				return true;
			}

			@Override
			public boolean shouldEnablePrivateGroupsInCore() {
				return true;
			}

			@Override
			public boolean shouldEnableForumsInCore() {
				return true;
			}

			@Override
			public boolean shouldEnableBlogsInCore() {
				return true;
			}
		};
	}
}




