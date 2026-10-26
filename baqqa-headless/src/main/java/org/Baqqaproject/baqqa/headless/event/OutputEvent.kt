package org.Baqqaproject.Baqqa.headless.event

import org.Baqqaproject.Baqqa.api.blog.BlogInvitationRequest
import org.Baqqaproject.Baqqa.api.blog.BlogInvitationResponse
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent
import org.Baqqaproject.Baqqa.api.forum.ForumInvitationRequest
import org.Baqqaproject.Baqqa.api.forum.ForumInvitationResponse
import org.Baqqaproject.Baqqa.api.introduction.IntroductionRequest
import org.Baqqaproject.Baqqa.api.introduction.IntroductionResponse
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageHeader
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationRequest
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationResponse
import org.Baqqaproject.Baqqa.headless.json.JsonDict
import org.Baqqaproject.Baqqa.headless.messaging.output
import javax.annotation.concurrent.Immutable

@Immutable
@Suppress("unused")
internal class OutputEvent(val name: String, val data: JsonDict) {
    val type = "event"
}

internal fun ConversationMessageReceivedEvent<*>.output(text: String?): JsonDict {
    check(messageHeader is PrivateMessageHeader)
    return (messageHeader as PrivateMessageHeader).output(contactId, text)
}

internal fun ConversationMessageReceivedEvent<*>.output() = when (messageHeader) {
    // requests
    is ForumInvitationRequest -> (messageHeader as ForumInvitationRequest).output(contactId)
    is BlogInvitationRequest -> (messageHeader as BlogInvitationRequest).output(contactId)
    is GroupInvitationRequest -> (messageHeader as GroupInvitationRequest).output(contactId)
    is IntroductionRequest -> (messageHeader as IntroductionRequest).output(contactId)
    // responses
    is ForumInvitationResponse -> (messageHeader as ForumInvitationResponse).output(contactId)
    is BlogInvitationResponse -> (messageHeader as BlogInvitationResponse).output(contactId)
    is GroupInvitationResponse -> (messageHeader as GroupInvitationResponse).output(contactId)
    is IntroductionResponse -> (messageHeader as IntroductionResponse).output(contactId)
    // unknown
    else -> throw IllegalStateException()
}



