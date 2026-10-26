package org.Baqqaproject.Baqqa.headless

import org.Baqqaproject.bramble.api.account.AccountManager
import org.Baqqaproject.bramble.api.crypto.DecryptionException
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager
import javax.annotation.concurrent.Immutable
import javax.inject.Inject
import javax.inject.Singleton

const val user = "user"
const val pass = "pass"

@Immutable
@Singleton
internal class BaqqaTestServiceImpl
@Inject
constructor(
    private val accountManager: AccountManager,
    private val lifecycleManager: LifecycleManager
) : BaqqaService {

    override fun start() {
        if (accountManager.accountExists()) {
            accountManager.deleteAccount()
        }
        accountManager.createAccount(user, pass)
        try {
            accountManager.signIn(pass)
        } catch (e: DecryptionException) {
            throw AssertionError("Password invalid")
        }
        val dbKey = accountManager.databaseKey ?: throw AssertionError()
        lifecycleManager.startServices(dbKey)
        lifecycleManager.waitForStartup()
    }

    override fun stop() {
        lifecycleManager.stopServices()
        lifecycleManager.waitForShutdown()
    }

}



