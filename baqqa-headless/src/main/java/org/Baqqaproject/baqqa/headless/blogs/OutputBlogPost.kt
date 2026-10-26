package org.Baqqaproject.Baqqa.headless.blogs

import org.Baqqaproject.bramble.identity.output
import org.Baqqaproject.Baqqa.api.blog.BlogPostHeader
import org.Baqqaproject.Baqqa.api.blog.MessageType
import org.Baqqaproject.Baqqa.headless.json.JsonDict
import java.util.Locale

internal fun BlogPostHeader.output(text: String) = JsonDict(
    "text" to text,
    "author" to author.output(),
    "authorStatus" to authorInfo.status.output(),
    "type" to type.output(),
    "id" to id.bytes,
    "parentId" to parentId?.bytes,
    "read" to isRead,
    "rssFeed" to isRssFeed,
    "timestamp" to timestamp,
    "timestampReceived" to timeReceived
)

internal fun MessageType.output() = name.lowercase(Locale.US)



