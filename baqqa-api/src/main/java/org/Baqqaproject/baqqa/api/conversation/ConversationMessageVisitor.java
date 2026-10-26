package org.Baqqaproject.Baqqa.api.conversation;

import org.Baqqaproject.Baqqa.api.blog.BlogInvitationRequest;
import org.Baqqaproject.Baqqa.api.blog.BlogInvitationResponse;
import org.Baqqaproject.Baqqa.api.forum.ForumInvitationRequest;
import org.Baqqaproject.Baqqa.api.forum.ForumInvitationResponse;
import org.Baqqaproject.Baqqa.api.introduction.IntroductionRequest;
import org.Baqqaproject.Baqqa.api.introduction.IntroductionResponse;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageHeader;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationRequest;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationResponse;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface ConversationMessageVisitor<T> {

	T visitPrivateMessageHeader(PrivateMessageHeader h);

	T visitBlogInvitationRequest(BlogInvitationRequest r);

	T visitBlogInvitationResponse(BlogInvitationResponse r);

	T visitForumInvitationRequest(ForumInvitationRequest r);

	T visitForumInvitationResponse(ForumInvitationResponse r);

	T visitGroupInvitationRequest(GroupInvitationRequest r);

	T visitGroupInvitationResponse(GroupInvitationResponse r);

	T visitIntroductionRequest(IntroductionRequest r);

	T visitIntroductionResponse(IntroductionResponse r);
}




