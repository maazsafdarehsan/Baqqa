package org.Baqqaproject.Baqqa.android;

import org.Baqqaproject.bramble.BrambleAndroidModule;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.account.BaqqaAccountModule;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.plugin.file.RemovableDriveModule;
import org.Baqqaproject.bramble.system.ClockModule;
import org.Baqqaproject.Baqqa.BaqqaCoreModule;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentModule;
import org.Baqqaproject.Baqqa.android.attachment.media.MediaModule;
import org.Baqqaproject.Baqqa.android.conversation.ConversationActivityScreenshotTest;
import org.Baqqaproject.Baqqa.android.settings.SettingsActivityScreenshotTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		AppModule.class,
		AttachmentModule.class,
		ClockModule.class,
		MediaModule.class,
		RemovableDriveModule.class,
		BaqqaCoreModule.class,
		BrambleAndroidModule.class,
		BaqqaAccountModule.class,
		BrambleCoreModule.class,
		ModularMailboxModule.class
})
public interface BaqqaUiTestComponent extends AndroidComponent {

	void inject(SetupDataTest test);

	void inject(ConversationActivityScreenshotTest test);

	void inject(SettingsActivityScreenshotTest test);

	void inject(PromoVideoTest test);

}



