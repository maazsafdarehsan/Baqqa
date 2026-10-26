package org.Baqqaproject.Baqqa;

import org.Baqqaproject.Baqqa.autodelete.AutoDeleteModule;
import org.Baqqaproject.Baqqa.avatar.AvatarModule;
import org.Baqqaproject.Baqqa.blog.BlogModule;
import org.Baqqaproject.Baqqa.conversation.ConversationModule;
import org.Baqqaproject.Baqqa.feed.FeedModule;
import org.Baqqaproject.Baqqa.forum.ForumModule;
import org.Baqqaproject.Baqqa.identity.IdentityModule;
import org.Baqqaproject.Baqqa.introduction.IntroductionModule;
import org.Baqqaproject.Baqqa.messaging.MessagingModule;
import org.Baqqaproject.Baqqa.privategroup.PrivateGroupModule;
import org.Baqqaproject.Baqqa.privategroup.invitation.GroupInvitationModule;
import org.Baqqaproject.Baqqa.sharing.SharingModule;

public interface BaqqaCoreEagerSingletons {

	void inject(AutoDeleteModule.EagerSingletons init);

	void inject(AvatarModule.EagerSingletons init);

	void inject(BlogModule.EagerSingletons init);

	void inject(ConversationModule.EagerSingletons init);

	void inject(FeedModule.EagerSingletons init);

	void inject(ForumModule.EagerSingletons init);

	void inject(GroupInvitationModule.EagerSingletons init);

	void inject(IdentityModule.EagerSingletons init);

	void inject(IntroductionModule.EagerSingletons init);

	void inject(MessagingModule.EagerSingletons init);

	void inject(PrivateGroupModule.EagerSingletons init);

	void inject(SharingModule.EagerSingletons init);

	class Helper {

		public static void injectEagerSingletons(BaqqaCoreEagerSingletons c) {
			c.inject(new AutoDeleteModule.EagerSingletons());
			c.inject(new AvatarModule.EagerSingletons());
			c.inject(new BlogModule.EagerSingletons());
			c.inject(new ConversationModule.EagerSingletons());
			c.inject(new FeedModule.EagerSingletons());
			c.inject(new ForumModule.EagerSingletons());
			c.inject(new GroupInvitationModule.EagerSingletons());
			c.inject(new MessagingModule.EagerSingletons());
			c.inject(new PrivateGroupModule.EagerSingletons());
			c.inject(new SharingModule.EagerSingletons());
			c.inject(new IdentityModule.EagerSingletons());
			c.inject(new IntroductionModule.EagerSingletons());
		}
	}
}



