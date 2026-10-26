package org.Baqqaproject.Baqqa;

import org.Baqqaproject.Baqqa.attachment.AttachmentModule;
import org.Baqqaproject.Baqqa.autodelete.AutoDeleteModule;
import org.Baqqaproject.Baqqa.avatar.AvatarModule;
import org.Baqqaproject.Baqqa.blog.BlogModule;
import org.Baqqaproject.Baqqa.client.BaqqaClientModule;
import org.Baqqaproject.Baqqa.conversation.ConversationModule;
import org.Baqqaproject.Baqqa.feed.FeedModule;
import org.Baqqaproject.Baqqa.forum.ForumModule;
import org.Baqqaproject.Baqqa.identity.IdentityModule;
import org.Baqqaproject.Baqqa.introduction.IntroductionModule;
import org.Baqqaproject.Baqqa.messaging.MessagingModule;
import org.Baqqaproject.Baqqa.privategroup.PrivateGroupModule;
import org.Baqqaproject.Baqqa.privategroup.invitation.GroupInvitationModule;
import org.Baqqaproject.Baqqa.sharing.SharingModule;
import org.Baqqaproject.Baqqa.test.TestModule;

import dagger.Module;

@Module(includes = {
		AttachmentModule.class,
		AutoDeleteModule.class,
		AvatarModule.class,
		BlogModule.class,
		BaqqaClientModule.class,
		ConversationModule.class,
		FeedModule.class,
		ForumModule.class,
		GroupInvitationModule.class,
		IdentityModule.class,
		IntroductionModule.class,
		MessagingModule.class,
		PrivateGroupModule.class,
		SharingModule.class,
		TestModule.class
})
public class BaqqaCoreModule {
}



