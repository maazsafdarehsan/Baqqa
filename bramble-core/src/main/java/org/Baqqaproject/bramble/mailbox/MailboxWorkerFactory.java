package org.Baqqaproject.bramble.mailbox;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.mailbox.MailboxFolderId;
import org.Baqqaproject.bramble.api.mailbox.MailboxProperties;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@NotNullByDefault
interface MailboxWorkerFactory {

	MailboxWorker createUploadWorker(ConnectivityChecker connectivityChecker,
			MailboxProperties properties, MailboxFolderId folderId,
			ContactId contactId);

	MailboxWorker createDownloadWorkerForContactMailbox(
			ConnectivityChecker connectivityChecker,
			TorReachabilityMonitor reachabilityMonitor,
			MailboxProperties properties);

	MailboxWorker createDownloadWorkerForOwnMailbox(
			ConnectivityChecker connectivityChecker,
			TorReachabilityMonitor reachabilityMonitor,
			MailboxProperties properties);

	MailboxWorker createContactListWorkerForOwnMailbox(
			ConnectivityChecker connectivityChecker,
			MailboxProperties properties);
}




