package org.Baqqaproject.Baqqa.headless

import com.fasterxml.jackson.databind.ObjectMapper
import dagger.Module
import dagger.Provides
import org.Baqqaproject.bramble.account.AccountModule
import org.Baqqaproject.bramble.api.db.DatabaseConfig
import org.Baqqaproject.bramble.api.mailbox.MailboxDirectory
import org.Baqqaproject.bramble.api.plugin.PluginConfig
import org.Baqqaproject.bramble.api.plugin.TorConstants.DEFAULT_CONTROL_PORT
import org.Baqqaproject.bramble.api.plugin.TorConstants.DEFAULT_SOCKS_PORT
import org.Baqqaproject.bramble.api.plugin.TorControlPort
import org.Baqqaproject.bramble.api.plugin.TorSocksPort
import org.Baqqaproject.bramble.api.plugin.TransportId
import org.Baqqaproject.bramble.api.plugin.duplex.DuplexPluginFactory
import org.Baqqaproject.bramble.api.plugin.simplex.SimplexPluginFactory
import org.Baqqaproject.bramble.event.DefaultEventExecutorModule
import org.Baqqaproject.bramble.system.ClockModule
import org.Baqqaproject.bramble.system.DefaultTaskSchedulerModule
import org.Baqqaproject.bramble.system.DefaultThreadFactoryModule
import org.Baqqaproject.bramble.system.DefaultWakefulIoExecutorModule
import org.Baqqaproject.bramble.test.TestFeatureFlagModule
import org.Baqqaproject.bramble.test.TestSecureRandomModule
import org.Baqqaproject.Baqqa.api.test.TestAvatarCreator
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
        DefaultEventExecutorModule::class,
        DefaultTaskSchedulerModule::class,
        DefaultWakefulIoExecutorModule::class,
        DefaultThreadFactoryModule::class,
        TestFeatureFlagModule::class,
        TestSecureRandomModule::class,
        HeadlessBlogModule::class,
        HeadlessContactModule::class,
        HeadlessEventModule::class,
        HeadlessForumModule::class,
        HeadlessMessagingModule::class
    ]
)
internal class HeadlessTestModule(private val appDir: File) {

    @Provides
    @Singleton
    internal fun provideBaqqaService(BaqqaService: BaqqaTestServiceImpl): BaqqaService =
        BaqqaService

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
    @TorSocksPort
    internal fun provideTorSocksPort(): Int = DEFAULT_SOCKS_PORT

    @Provides
    @TorControlPort
    internal fun provideTorControlPort(): Int = DEFAULT_CONTROL_PORT

    @Provides
    @Singleton
    internal fun providePluginConfig(): PluginConfig {
        return object : PluginConfig {
            override fun getDuplexFactories(): Collection<DuplexPluginFactory> = emptyList()
            override fun getSimplexFactories(): Collection<SimplexPluginFactory> = emptyList()
            override fun shouldPoll(): Boolean = false
            override fun getTransportPreferences(): Map<TransportId, List<TransportId>> = emptyMap()
        }
    }

    @Provides
    @Singleton
    internal fun provideObjectMapper() = ObjectMapper()

    @Provides
    internal fun provideTestAvatarCreator() = TestAvatarCreator { null }
}



