package org.Baqqaproject.bramble.test;

import org.Baqqaproject.bramble.api.mailbox.MailboxDirectory;

import java.io.File;

import dagger.Module;
import dagger.Provides;

@Module
public class TestMailboxDirectoryModule {

	@Provides
	@MailboxDirectory
	File provideMailboxDirectory() {
		return new File("mailbox");
	}
}



