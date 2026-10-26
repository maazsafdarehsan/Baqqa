package org.Baqqaproject.bramble.identity

import org.Baqqaproject.bramble.api.identity.Author
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo
import org.Baqqaproject.Baqqa.headless.json.JsonDict
import java.util.Locale

fun Author.output() = JsonDict(
    "formatVersion" to formatVersion,
    "id" to id.bytes,
    "name" to name,
    "publicKey" to publicKey.encoded
)

fun AuthorInfo.Status.output() = name.lowercase(Locale.US)



