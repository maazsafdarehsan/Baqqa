package org.Baqqaproject.Baqqa.headless.forums

import org.Baqqaproject.Baqqa.api.forum.Forum
import org.Baqqaproject.Baqqa.headless.json.JsonDict

internal fun Forum.output() = JsonDict(
    "name" to name,
    "id" to id.bytes
)

internal fun Collection<Forum>.output() = map { it.output() }



