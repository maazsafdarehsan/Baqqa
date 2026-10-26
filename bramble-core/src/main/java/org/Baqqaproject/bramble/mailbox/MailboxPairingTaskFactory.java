package org.Baqqaproject.bramble.mailbox;


import org.Baqqaproject.bramble.api.mailbox.MailboxPairingTask;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface MailboxPairingTaskFactory {

	MailboxPairingTask createPairingTask(String qrCodePayload);

}




