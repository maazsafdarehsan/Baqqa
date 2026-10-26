package org.Baqqaproject.Baqqa.test;

import org.Baqqaproject.bramble.BrambleCoreIntegrationTestEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.identity.AuthorFactory;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.properties.TransportPropertyManager;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.test.BrambleCoreIntegrationTestModule;
import org.Baqqaproject.bramble.test.BrambleIntegrationTestComponent;
import org.Baqqaproject.bramble.test.TestDnsModule;
import org.Baqqaproject.bramble.test.TestPluginConfigModule;
import org.Baqqaproject.bramble.test.TestSocksModule;
import org.Baqqaproject.bramble.test.TimeTravel;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentReader;
import org.Baqqaproject.Baqqa.api.autodelete.AutoDeleteManager;
import org.Baqqaproject.Baqqa.api.avatar.AvatarManager;
import org.Baqqaproject.Baqqa.api.blog.BlogFactory;
import org.Baqqaproject.Baqqa.api.blog.BlogManager;
import org.Baqqaproject.Baqqa.api.blog.BlogSharingManager;
import org.Baqqaproject.Baqqa.api.client.MessageTracker;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager;
import org.Baqqaproject.Baqqa.api.forum.ForumManager;
import org.Baqqaproject.Baqqa.api.forum.ForumSharingManager;
import org.Baqqaproject.Baqqa.api.introduction.IntroductionManager;
import org.Baqqaproject.Baqqa.api.messaging.MessagingManager;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFactory;
import org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupManager;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationFactory;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationManager;
import org.Baqqaproject.Baqqa.attachment.AttachmentModule;
import org.Baqqaproject.Baqqa.autodelete.AutoDeleteModule;
import org.Baqqaproject.Baqqa.avatar.AvatarModule;
import org.Baqqaproject.Baqqa.blog.BlogModule;
import org.Baqqaproject.Baqqa.client.BaqqaClientModule;
import org.Baqqaproject.Baqqa.conversation.ConversationModule;
import org.Baqqaproject.Baqqa.forum.ForumModule;
import org.Baqqaproject.Baqqa.identity.IdentityModule;
import org.Baqqaproject.Baqqa.introduction.IntroductionModule;
import org.Baqqaproject.Baqqa.messaging.MessagingModule;
import org.Baqqaproject.Baqqa.privategroup.PrivateGroupModule;
import org.Baqqaproject.Baqqa.privategroup.invitation.GroupInvitationModule;
import org.Baqqaproject.Baqqa.sharing.SharingModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		BrambleCoreIntegrationTestModule.class,
		BrambleCoreModule.class,
		AttachmentModule.class,
		AutoDeleteModule.class,
		AvatarModule.class,
		BlogModule.class,
		BaqqaClientModule.class,
		ConversationModule.class,
		ForumModule.class,
		GroupInvitationModule.class,
		IdentityModule.class,
		IntroductionModule.class,
		MessagingModule.class,
		PrivateGroupModule.class,
		SharingModule.class,
		ModularMailboxModule.class,
		TestDnsModule.class,
		TestSocksModule.class,
		TestPluginConfigModule.class,
})
public interface BaqqaIntegrationTestComponent
		extends BrambleIntegrationTestComponent {

	void inject(BaqqaIntegrationTest<BaqqaIntegrationTestComponent> init);

	void inject(AutoDeleteModule.EagerSingletons init);

	void inject(AvatarModule.EagerSingletons init);

	void inject(BlogModule.EagerSingletons init);

	void inject(ConversationModule.EagerSingletons init);

	void inject(ForumModule.EagerSingletons init);

	void inject(GroupInvitationModule.EagerSingletons init);

	void inject(IdentityModule.EagerSingletons init);

	void inject(IntroductionModule.EagerSingletons init);

	void inject(MessagingModule.EagerSingletons init);

	void inject(PrivateGroupModule.EagerSingletons init);

	void inject(SharingModule.EagerSingletons init);

	LifecycleManager getLifecycleManager();

	AttachmentReader getAttachmentReader();

	AvatarManager getAvatarManager();

	ContactManager getContactManager();

	ConversationManager getConversationManager();

	DatabaseComponent getDatabaseComponent();

	BlogManager getBlogManager();

	BlogSharingManager getBlogSharingManager();

	ForumSharingManager getForumSharingManager();

	ForumManager getForumManager();

	GroupInvitationManager getGroupInvitationManager();

	GroupInvitationFactory getGroupInvitationFactory();

	IntroductionManager getIntroductionManager();

	MessageTracker getMessageTracker();

	MessagingManager getMessagingManager();

	PrivateGroupManager getPrivateGroupManager();

	PrivateMessageFactory getPrivateMessageFactory();

	TransportPropertyManager getTransportPropertyManager();

	AuthorFactory getAuthorFactory();

	BlogFactory getBlogFactory();

	AutoDeleteManager getAutoDeleteManager();

	Clock getClock();

	TimeTravel getTimeTravel();

	class Helper {

		public static void injectEagerSingletons(
				BaqqaIntegrationTestComponent c) {
			BrambleCoreIntegrationTestEagerSingletons.Helper
					.injectEagerSingletons(c);
			c.inject(new AutoDeleteModule.EagerSingletons());
			c.inject(new AvatarModule.EagerSingletons());
			c.inject(new BlogModule.EagerSingletons());
			c.inject(new ConversationModule.EagerSingletons());
			c.inject(new ForumModule.EagerSingletons());
			c.inject(new GroupInvitationModule.EagerSingletons());
			c.inject(new IdentityModule.EagerSingletons());
			c.inject(new IntroductionModule.EagerSingletons());
			c.inject(new MessagingModule.EagerSingletons());
			c.inject(new PrivateGroupModule.EagerSingletons());
			c.inject(new SharingModule.EagerSingletons());
		}
	}
}



