package org.Baqqaproject.Baqqa.android;

import org.briarproject.android.dontkillmelib.wakelock.AndroidWakeLockManager;
import org.Baqqaproject.bramble.BrambleAndroidEagerSingletons;
import org.Baqqaproject.bramble.BrambleAndroidModule;
import org.Baqqaproject.bramble.BrambleAppComponent;
import org.Baqqaproject.bramble.BrambleCoreEagerSingletons;
import org.Baqqaproject.bramble.BrambleCoreModule;
import org.Baqqaproject.bramble.account.BaqqaAccountModule;
import org.Baqqaproject.bramble.api.FeatureFlags;
import org.Baqqaproject.bramble.api.account.AccountManager;
import org.Baqqaproject.bramble.api.connection.ConnectionRegistry;
import org.Baqqaproject.bramble.api.contact.ContactExchangeManager;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.crypto.CryptoExecutor;
import org.Baqqaproject.bramble.api.crypto.PasswordStrengthEstimator;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.TransactionManager;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.identity.IdentityManager;
import org.Baqqaproject.bramble.api.keyagreement.KeyAgreementTask;
import org.Baqqaproject.bramble.api.keyagreement.PayloadEncoder;
import org.Baqqaproject.bramble.api.keyagreement.PayloadParser;
import org.Baqqaproject.bramble.api.lifecycle.IoExecutor;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.plugin.PluginManager;
import org.Baqqaproject.bramble.api.settings.SettingsManager;
import org.Baqqaproject.bramble.api.system.AndroidExecutor;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.bramble.mailbox.ModularMailboxModule;
import org.Baqqaproject.bramble.plugin.file.RemovableDriveModule;
import org.Baqqaproject.bramble.system.ClockModule;
import org.Baqqaproject.Baqqa.BaqqaCoreEagerSingletons;
import org.Baqqaproject.Baqqa.BaqqaCoreModule;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentModule;
import org.Baqqaproject.Baqqa.android.attachment.media.MediaModule;
import org.Baqqaproject.Baqqa.android.contact.connect.BluetoothIntroFragment;
import org.Baqqaproject.Baqqa.android.conversation.glide.BaqqaModelLoader;
import org.Baqqaproject.Baqqa.android.hotspot.AbstractTabsFragment;
import org.Baqqaproject.Baqqa.android.hotspot.FallbackFragment;
import org.Baqqaproject.Baqqa.android.hotspot.HotspotIntroFragment;
import org.Baqqaproject.Baqqa.android.hotspot.ManualHotspotFragment;
import org.Baqqaproject.Baqqa.android.hotspot.QrHotspotFragment;
import org.Baqqaproject.Baqqa.android.logging.CachingLogHandler;
import org.Baqqaproject.Baqqa.android.login.SignInReminderReceiver;
import org.Baqqaproject.Baqqa.android.mailbox.ErrorFragment;
import org.Baqqaproject.Baqqa.android.mailbox.ErrorWizardFragment;
import org.Baqqaproject.Baqqa.android.mailbox.MailboxScanFragment;
import org.Baqqaproject.Baqqa.android.mailbox.MailboxStatusFragment;
import org.Baqqaproject.Baqqa.android.mailbox.OfflineFragment;
import org.Baqqaproject.Baqqa.android.mailbox.SetupDownloadFragment;
import org.Baqqaproject.Baqqa.android.mailbox.SetupIntroFragment;
import org.Baqqaproject.Baqqa.android.removabledrive.ChooserFragment;
import org.Baqqaproject.Baqqa.android.removabledrive.ReceiveFragment;
import org.Baqqaproject.Baqqa.android.removabledrive.SendFragment;
import org.Baqqaproject.Baqqa.android.settings.ConnectionsFragment;
import org.Baqqaproject.Baqqa.android.settings.NotificationsFragment;
import org.Baqqaproject.Baqqa.android.settings.SecurityFragment;
import org.Baqqaproject.Baqqa.android.settings.SettingsFragment;
import org.Baqqaproject.Baqqa.android.view.EmojiTextInputView;
import org.Baqqaproject.Baqqa.api.android.AndroidNotificationManager;
import org.Baqqaproject.Baqqa.api.android.DozeWatchdog;
import org.Baqqaproject.Baqqa.api.android.LockManager;
import org.Baqqaproject.Baqqa.api.android.ScreenFilterMonitor;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentReader;
import org.Baqqaproject.Baqqa.api.autodelete.AutoDeleteManager;
import org.Baqqaproject.Baqqa.api.blog.BlogManager;
import org.Baqqaproject.Baqqa.api.blog.BlogPostFactory;
import org.Baqqaproject.Baqqa.api.blog.BlogSharingManager;
import org.Baqqaproject.Baqqa.api.client.MessageTracker;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager;
import org.Baqqaproject.Baqqa.api.feed.FeedManager;
import org.Baqqaproject.Baqqa.api.forum.ForumManager;
import org.Baqqaproject.Baqqa.api.forum.ForumSharingManager;
import org.Baqqaproject.Baqqa.api.identity.AuthorManager;
import org.Baqqaproject.Baqqa.api.introduction.IntroductionManager;
import org.Baqqaproject.Baqqa.api.messaging.MessagingManager;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFactory;
import org.Baqqaproject.Baqqa.api.privategroup.GroupMessageFactory;
import org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupFactory;
import org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupManager;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationFactory;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationManager;
import org.Baqqaproject.Baqqa.api.test.TestDataCreator;
import org.briarproject.onionwrapper.CircumventionProvider;
import org.briarproject.onionwrapper.LocationUtils;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Component;

@Singleton
@Component(modules = {
		BrambleCoreModule.class,
		BaqqaCoreModule.class,
		BrambleAndroidModule.class,
		BaqqaAccountModule.class,
		AppModule.class,
		AttachmentModule.class,
		ClockModule.class,
		MediaModule.class,
		ModularMailboxModule.class,
		RemovableDriveModule.class
})
public interface AndroidComponent
		extends BrambleCoreEagerSingletons, BrambleAndroidEagerSingletons,
		BaqqaCoreEagerSingletons, AndroidEagerSingletons, BrambleAppComponent {

	// Exposed objects
	@CryptoExecutor
	Executor cryptoExecutor();

	PasswordStrengthEstimator passwordStrengthIndicator();

	@DatabaseExecutor
	Executor databaseExecutor();

	TransactionManager transactionManager();

	MessageTracker messageTracker();

	LifecycleManager lifecycleManager();

	IdentityManager identityManager();

	AttachmentReader attachmentReader();

	AuthorManager authorManager();

	PluginManager pluginManager();

	EventBus eventBus();

	AndroidNotificationManager androidNotificationManager();

	ScreenFilterMonitor screenFilterMonitor();

	ConnectionRegistry connectionRegistry();

	ContactManager contactManager();

	ConversationManager conversationManager();

	MessagingManager messagingManager();

	PrivateMessageFactory privateMessageFactory();

	PrivateGroupManager privateGroupManager();

	GroupInvitationFactory groupInvitationFactory();

	GroupInvitationManager groupInvitationManager();

	PrivateGroupFactory privateGroupFactory();

	GroupMessageFactory groupMessageFactory();

	ForumManager forumManager();

	ForumSharingManager forumSharingManager();

	BlogSharingManager blogSharingManager();

	BlogManager blogManager();

	BlogPostFactory blogPostFactory();

	SettingsManager settingsManager();

	ContactExchangeManager contactExchangeManager();

	KeyAgreementTask keyAgreementTask();

	PayloadEncoder payloadEncoder();

	PayloadParser payloadParser();

	IntroductionManager introductionManager();

	AndroidExecutor androidExecutor();

	FeedManager feedManager();

	Clock clock();

	TestDataCreator testDataCreator();

	DozeWatchdog dozeWatchdog();

	@IoExecutor
	Executor ioExecutor();

	AccountManager accountManager();

	LockManager lockManager();

	LocationUtils locationUtils();

	CircumventionProvider circumventionProvider();

	ViewModelProvider.Factory viewModelFactory();

	FeatureFlags featureFlags();

	AndroidWakeLockManager wakeLockManager();

	CachingLogHandler logHandler();

	Thread.UncaughtExceptionHandler exceptionHandler();

	AutoDeleteManager autoDeleteManager();

	void inject(SignInReminderReceiver BaqqaService);

	void inject(BaqqaService BaqqaService);

	void inject(NotificationCleanupService notificationCleanupService);

	void inject(EmojiTextInputView textInputView);

	void inject(BaqqaModelLoader BaqqaModelLoader);

	void inject(SettingsFragment settingsFragment);

	void inject(ConnectionsFragment connectionsFragment);

	void inject(SecurityFragment securityFragment);

	void inject(NotificationsFragment notificationsFragment);

	void inject(HotspotIntroFragment hotspotIntroFragment);

	void inject(AbstractTabsFragment abstractTabsFragment);

	void inject(QrHotspotFragment qrHotspotFragment);

	void inject(ManualHotspotFragment manualHotspotFragment);

	void inject(FallbackFragment fallbackFragment);

	void inject(ChooserFragment chooserFragment);

	void inject(SendFragment sendFragment);

	void inject(ReceiveFragment receiveFragment);

	void inject(BluetoothIntroFragment bluetoothIntroFragment);

	void inject(SetupIntroFragment setupIntroFragment);

	void inject(SetupDownloadFragment setupDownloadFragment);

	void inject(MailboxScanFragment mailboxScanFragment);

	void inject(OfflineFragment offlineFragment);

	void inject(ErrorFragment errorFragment);

	void inject(MailboxStatusFragment mailboxStatusFragment);

	void inject(ErrorWizardFragment errorWizardFragment);
}




