package org.Baqqaproject.Baqqa.headless

import org.Baqqaproject.bramble.api.crypto.KeyStrengthener
import org.Baqqaproject.bramble.api.db.DatabaseConfig
import java.io.File

internal class HeadlessDatabaseConfig(private val dbDir: File, private val keyDir: File) :
    DatabaseConfig {

    override fun getDatabaseDirectory() = dbDir

    override fun getDatabaseKeyDirectory() = keyDir

    override fun getKeyStrengthener(): KeyStrengthener? = null
}



