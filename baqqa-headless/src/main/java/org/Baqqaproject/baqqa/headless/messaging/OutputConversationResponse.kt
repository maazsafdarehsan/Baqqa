package org.Baqqaproject.Baqqa.headless.messaging

import org.Baqqaproject.bramble.api.contact.ContactId
import org.Baqqaproject.bramble.identity.output
import org.Baqqaproject.Baqqa.api.blog.BlogInvitationResponse
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageHeader
import org.Baqqaproject.Baqqa.api.conversation.ConversationResponse
import org.Baqqaproject.Baqqa.api.forum.ForumInvitationResponse
import org.Baqqaproject.Baqqa.api.introduction.IntroductionResponse
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationResponse
import org.Baqqaproject.Baqqa.api.sharing.InvitationResponse
import org.Baqqaproject.Baqqa.headless.json.JsonDict

internal fun ConversationResponse.output(contactId: ContactId): JsonDict {
    val dict = (this as ConversationMessageHeader).output(contactId)
    dict.putAll(
        "sessionId" to sessionId.bytes,
        "accepted" to wasAccepted()
    )
    return dict
}

internal fun IntroductionResponse.output(contactId: ContactId): JsonDict {
    val dict = (this as ConversationResponse).output(contactId)
    dict.putAll(
        "type" to "IntroductionResponse",
        "introducedAuthor" to introducedAuthor.output(),
        "introducer" to isIntroducer
    )
    return dict
}

internal fun InvitationResponse.output(contactId: ContactId): JsonDict {
    val dict = (this as ConversationResponse).output(contactId)
    dict["shareableId"] = shareableId.bytes
    return dict
}

internal fun BlogInvitationResponse.output(contactId: ContactId): JsonDict {
    val dict = (this as InvitationResponse).output(contactId)
    dict["type"] = "BlogInvitationResponse"
    return dict
}

internal fun ForumInvitationResponse.output(contactId: ContactId): JsonDict {
    val dict = (this as InvitationResponse).output(contactId)
    dict["type"] = "ForumInvitationResponse"
    return dict
}

internal fun GroupInvitationResponse.output(contactId: ContactId): JsonDict {
    val dict = (this as InvitationResponse).output(contactId)
    dict["type"] = "GroupInvitationResponse"
    return dict
}



