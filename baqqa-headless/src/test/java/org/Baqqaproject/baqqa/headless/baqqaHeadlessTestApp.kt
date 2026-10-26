package org.Baqqaproject.Baqqa.headless

import dagger.Component
import org.Baqqaproject.bramble.BrambleCoreEagerSingletons
import org.Baqqaproject.bramble.BrambleCoreModule
import org.Baqqaproject.bramble.BrambleJavaEagerSingletons
import org.Baqqaproject.bramble.BrambleJavaModule
import org.Baqqaproject.bramble.api.crypto.CryptoComponent
import org.Baqqaproject.Baqqa.BaqqaCoreEagerSingletons
import org.Baqqaproject.Baqqa.BaqqaCoreModule
import org.Baqqaproject.Baqqa.api.test.TestDataCreator
import javax.inject.Singleton

@Component(
    modules = [
        BrambleCoreModule::class,
        BrambleJavaModule::class,
        BaqqaCoreModule::class,
        HeadlessTestModule::class
    ]
)
@Singleton
internal interface BaqqaHeadlessTestApp : BrambleCoreEagerSingletons, BaqqaCoreEagerSingletons,
    BrambleJavaEagerSingletons, HeadlessEagerSingletons {

    fun getRouter(): Router

    fun getCryptoComponent(): CryptoComponent

    fun getTestDataCreator(): TestDataCreator
}



