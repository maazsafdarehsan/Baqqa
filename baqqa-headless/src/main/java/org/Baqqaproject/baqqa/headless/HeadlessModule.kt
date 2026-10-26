package org.Baqqaproject.Baqqa.headless

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import org.Baqqaproject.bramble.account.AccountModule
import org.Baqqaproject.bramble.api.FeatureFlags
import org.Baqqaproject.bramble.api.db.DatabaseConfig
import org.Baqqaproject.bramble.api.mailbox.MailboxDirectory
import org.Baqqaproject.bramble.api.plugin.PluginConfig
import org.Baqqaproject.bramble.api.plugin.TorConstants.DEFAULT_CONTROL_PORT
import org.Baqqaproject.bramble.api.plugin.TorConstants.DEFAULT_SOCKS_PORT
import org.Baqqaproject.bramble.api.plugin.TorControlPort
import org.Baqqaproject.bramble.api.plugin.TorDirectory
import org.Baqqaproject.bramble.api.plugin.TorSocksPort
import org.Baqqaproject.bramble.api.plugin.TransportId
import org.Baqqaproject.bramble.api.plugin.duplex.DuplexPluginFactory
import org.Baqqaproject.bramble.api.plugin.simplex.SimplexPluginFactory
import org.Baqqaproject.bramble.battery.DefaultBatteryManagerModule
import org.Baqqaproject.bramble.event.DefaultEventExecutorModule
import org.Baqqaproject.bramble.plugin.tor.MacTorPluginFactory
import org.Baqqaproject.bramble.plugin.tor.UnixTorPluginFactory
import org.Baqqaproject.bramble.plugin.tor.WindowsTorPluginFactory
import org.Baqqaproject.bramble.system.ClockModule
import org.Baqqaproject.bramble.system.DefaultTaskSchedulerModule
import org.Baqqaproject.bramble.system.DefaultThreadFactoryModule
import org.Baqqaproject.bramble.system.DefaultWakefulIoExecutorModule
import org.Baqqaproject.bramble.system.DesktopSecureRandomModule
import org.Baqqaproject.bramble.util.OsUtils.isLinux
import org.Baqqaproject.bramble.util.OsUtils.isMac
import org.Baqqaproject.bramble.util.OsUtils.isWindows
import org.Baqqaproject.Baqqa.headless.blogs.HeadlessBlogModule
import org.Baqqaproject.Baqqa.headless.contact.HeadlessContactModule
import org.Baqqaproject.Baqqa.headless.event.HeadlessEventModule
import org.Baqqaproject.Baqqa.headless.forums.HeadlessForumModule
import org.Baqqaproject.Baqqa.headless.messaging.HeadlessMessagingModule
import java.io.File
import java.util.Collections.emptyList
import javax.inject.Singleton

@Module(
    includes = [
        AccountModule::class,
        ClockModule::class,
        DefaultBatteryManagerModule::class,
        DefaultEventExecutorModule::class,
        DefaultTaskSchedulerModule::class,
        DefaultWakefulIoExecutorModule::class,
        DefaultThreadFactoryModule::class,
        DesktopSecureRandomModule::class,
        HeadlessBlogModule::class,
        HeadlessContactModule::class,
        HeadlessEventModule::class,
        HeadlessForumModule::class,
        HeadlessMessagingModule::class
    ]
)
internal class HeadlessModule(private val appDir: File) {

    @Provides
    @Singleton
    internal fun provideBaqqaService(BaqqaService: BaqqaServiceImpl): BaqqaService = BaqqaService

    @Provides
    @Singleton
    internal fun provideDatabaseConfig(): DatabaseConfig {
        val dbDir = File(appDir, "db")
        val keyDir = File(appDir, "key")
        return HeadlessDatabaseConfig(dbDir, keyDir)
    }

    @Provides
    @MailboxDirectory
    internal fun provideMailboxDirectory(): File {
        return File(appDir, "mailbox")
    }

    @Provides
    @TorDirectory
    internal fun provideTorDirectory(): File {
        return File(appDir, "tor")
    }

    @Provides
    @TorSocksPort
    internal fun provideTorSocksPort(): Int = DEFAULT_SOCKS_PORT

    @Provides
    @TorControlPort
    internal fun provideTorControlPort(): Int = DEFAULT_CONTROL_PORT

    @Provides
    @Singleton
    internal fun providePluginConfig(
        unixTor: UnixTorPluginFactory,
        macTor: MacTorPluginFactory,
        winTor: WindowsTorPluginFactory
    ): PluginConfig {
        val duplex: List<DuplexPluginFactory> = when {
            isLinux() -> listOf(unixTor)
            isMac() -> listOf(macTor)
            isWindows() -> listOf(winTor)
            else -> emptyList()
        }
        return object : PluginConfig {
            override fun getDuplexFactories(): Collection<DuplexPluginFactory> = duplex
            override fun getSimplexFactories(): Collection<SimplexPluginFactory> = emptyList()
            override fun shouldPoll(): Boolean = true
            override fun getTransportPreferences(): Map<TransportId, List<TransportId>> = emptyMap()
        }
    }

    @Provides
    @Singleton
    internal fun provideObjectMapper() = ObjectMapper()

    @Provides
    internal fun provideFeatureFlags() = object : FeatureFlags {
        override fun shouldEnableImageAttachments() = false
        override fun shouldEnableProfilePictures() = false
        override fun shouldEnableDisappearingMessages() = false
        override fun shouldEnablePrivateGroupsInCore() = false
        override fun shouldEnableForumsInCore() = true
        override fun shouldEnableBlogsInCore() = true
    }
}



