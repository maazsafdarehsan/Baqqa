package org.Baqqaproject.Baqqa.headless.contact

import org.Baqqaproject.bramble.api.contact.Contact
import org.Baqqaproject.bramble.api.contact.event.ContactAddedEvent
import org.Baqqaproject.bramble.api.plugin.event.ContactConnectedEvent
import org.Baqqaproject.bramble.api.plugin.event.ContactDisconnectedEvent
import org.Baqqaproject.bramble.identity.output
import org.Baqqaproject.Baqqa.headless.json.JsonDict

internal fun Contact.output(latestMsgTime: Long, connected: Boolean, unreadCount: Int) = JsonDict(
    "contactId" to id.int,
    "author" to author.output(),
    "verified" to isVerified,
    "lastChatActivity" to latestMsgTime,
    "connected" to connected,
    "unreadCount" to unreadCount
).apply {
    alias?.let { put("alias", it) }
    handshakePublicKey?.let { put("handshakePublicKey", it.encoded) }
}

internal fun ContactAddedEvent.output() = JsonDict(
    "contactId" to contactId.int,
    "verified" to isVerified
)

internal fun ContactConnectedEvent.output() = JsonDict(
    "contactId" to contactId.int
)

internal fun ContactDisconnectedEvent.output() = JsonDict(
    "contactId" to contactId.int
)



