package org.Baqqaproject.Baqqa.introduction;

import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.test.BrambleCoreIntegrationTestModule;
import org.Baqqaproject.bramble.test.TestDnsModule;
import org.Baqqaproject.bramble.test.TestPluginConfigModule;
import org.Baqqaproject.bramble.test.TestSocksModule;
import org.Baqqaproject.Baqqa.attachment.AttachmentModule;
import org.Baqqaproject.Baqqa.autodelete.AutoDeleteModule;
import org.Baqqaproject.Baqqa.avatar.AvatarModule;
import org.Baqqaproject.Baqqa.blog.BlogModule;
import org.Baqqaproject.Baqqa.client.BaqqaClientModule;
import org.Baqqaproject.Baqqa.conversation.ConversationModule;
import org.Baqqaproject.Baqqa.forum.ForumModule;
import org.Baqqaproject.Baqqa.identity.IdentityModule;
import org.Baqqaproject.Baqqa.messaging.MessagingModule;
import org.Baqqaproject.Baqqa.privategroup.PrivateGroupModule;
import org.Baqqaproject.Baqqa.privategroup.invitation.GroupInvitationModule;
import org.Baqqaproject.Baqqa.sharing.SharingModule;
import org.Baqqaproject.Baqqa.test.BaqqaIntegrationTestComponent;

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
interface IntroductionIntegrationTestComponent
		extends BaqqaIntegrationTestComponent {

	void inject(IntroductionIntegrationTest init);

	void inject(MessageEncoderParserIntegrationTest init);

	void inject(SessionEncoderParserIntegrationTest init);

	void inject(IntroductionCryptoIntegrationTest init);

	void inject(AutoDeleteIntegrationTest init);

	MessageEncoder getMessageEncoder();

	MessageParser getMessageParser();

	SessionParser getSessionParser();

	IntroductionCrypto getIntroductionCrypto();

}



