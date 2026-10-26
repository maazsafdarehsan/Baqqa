package org.Baqqaproject.Baqqa.headless

import dagger.Component
import org.Baqqaproject.bramble.BrambleCoreEagerSingletons
import org.Baqqaproject.bramble.BrambleCoreModule
import org.Baqqaproject.bramble.BrambleJavaEagerSingletons
import org.Baqqaproject.bramble.BrambleJavaModule
import org.Baqqaproject.Baqqa.BaqqaCoreEagerSingletons
import org.Baqqaproject.Baqqa.BaqqaCoreModule
import java.security.SecureRandom
import javax.inject.Singleton

@Component(
    modules = [
        BrambleCoreModule::class,
        BrambleJavaModule::class,
        BaqqaCoreModule::class,
        HeadlessModule::class
    ]
)
@Singleton
internal interface BaqqaHeadlessApp : BrambleCoreEagerSingletons, BaqqaCoreEagerSingletons,
    BrambleJavaEagerSingletons, HeadlessEagerSingletons {

    fun getRouter(): Router

    fun getSecureRandom(): SecureRandom
}



