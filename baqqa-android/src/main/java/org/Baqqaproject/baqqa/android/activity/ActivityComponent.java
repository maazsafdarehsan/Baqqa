package org.Baqqaproject.Baqqa.android.activity;

import android.app.Activity;

import org.Baqqaproject.Baqqa.android.AndroidComponent;
import org.Baqqaproject.Baqqa.android.StartupFailureActivity;
import org.Baqqaproject.Baqqa.android.account.SetupActivity;
import org.Baqqaproject.Baqqa.android.account.SetupFragment;
import org.Baqqaproject.Baqqa.android.account.UnlockActivity;
import org.Baqqaproject.Baqqa.android.blog.BlogActivity;
import org.Baqqaproject.Baqqa.android.blog.BlogFragment;
import org.Baqqaproject.Baqqa.android.blog.BlogPostFragment;
import org.Baqqaproject.Baqqa.android.blog.FeedFragment;
import org.Baqqaproject.Baqqa.android.blog.ReblogActivity;
import org.Baqqaproject.Baqqa.android.blog.ReblogFragment;
import org.Baqqaproject.Baqqa.android.blog.RssFeedActivity;
import org.Baqqaproject.Baqqa.android.blog.RssFeedDeleteFeedDialogFragment;
import org.Baqqaproject.Baqqa.android.blog.RssFeedImportFailedDialogFragment;
import org.Baqqaproject.Baqqa.android.blog.RssFeedImportFragment;
import org.Baqqaproject.Baqqa.android.blog.RssFeedManageFragment;
import org.Baqqaproject.Baqqa.android.blog.WriteBlogPostActivity;
import org.Baqqaproject.Baqqa.android.contact.ContactListFragment;
import org.Baqqaproject.Baqqa.android.contact.add.nearby.AddNearbyContactActivity;
import org.Baqqaproject.Baqqa.android.contact.add.nearby.AddNearbyContactErrorFragment;
import org.Baqqaproject.Baqqa.android.contact.add.nearby.AddNearbyContactFragment;
import org.Baqqaproject.Baqqa.android.contact.add.nearby.AddNearbyContactIntroFragment;
import org.Baqqaproject.Baqqa.android.contact.add.remote.AddContactActivity;
import org.Baqqaproject.Baqqa.android.contact.add.remote.LinkExchangeFragment;
import org.Baqqaproject.Baqqa.android.contact.add.remote.NicknameFragment;
import org.Baqqaproject.Baqqa.android.contact.add.remote.PendingContactListActivity;
import org.Baqqaproject.Baqqa.android.contact.connect.ConnectViaBluetoothActivity;
import org.Baqqaproject.Baqqa.android.conversation.AliasDialogFragment;
import org.Baqqaproject.Baqqa.android.conversation.ConversationActivity;
import org.Baqqaproject.Baqqa.android.conversation.ConversationSettingsDialog;
import org.Baqqaproject.Baqqa.android.conversation.ImageActivity;
import org.Baqqaproject.Baqqa.android.conversation.ImageFragment;
import org.Baqqaproject.Baqqa.android.forum.CreateForumActivity;
import org.Baqqaproject.Baqqa.android.forum.ForumActivity;
import org.Baqqaproject.Baqqa.android.forum.ForumListFragment;
import org.Baqqaproject.Baqqa.android.fragment.ScreenFilterDialogFragment;
import org.Baqqaproject.Baqqa.android.hotspot.HotspotActivity;
import org.Baqqaproject.Baqqa.android.introduction.ContactChooserFragment;
import org.Baqqaproject.Baqqa.android.introduction.IntroductionActivity;
import org.Baqqaproject.Baqqa.android.introduction.IntroductionMessageFragment;
import org.Baqqaproject.Baqqa.android.login.ChangePasswordActivity;
import org.Baqqaproject.Baqqa.android.login.OpenDatabaseFragment;
import org.Baqqaproject.Baqqa.android.login.PasswordFragment;
import org.Baqqaproject.Baqqa.android.login.StartupActivity;
import org.Baqqaproject.Baqqa.android.mailbox.MailboxActivity;
import org.Baqqaproject.Baqqa.android.navdrawer.NavDrawerActivity;
import org.Baqqaproject.Baqqa.android.navdrawer.TransportsActivity;
import org.Baqqaproject.Baqqa.android.panic.PanicPreferencesActivity;
import org.Baqqaproject.Baqqa.android.panic.PanicResponderActivity;
import org.Baqqaproject.Baqqa.android.privategroup.conversation.GroupActivity;
import org.Baqqaproject.Baqqa.android.privategroup.creation.CreateGroupActivity;
import org.Baqqaproject.Baqqa.android.privategroup.creation.CreateGroupFragment;
import org.Baqqaproject.Baqqa.android.privategroup.creation.CreateGroupModule;
import org.Baqqaproject.Baqqa.android.privategroup.creation.GroupInviteActivity;
import org.Baqqaproject.Baqqa.android.privategroup.creation.GroupInviteFragment;
import org.Baqqaproject.Baqqa.android.privategroup.invitation.GroupInvitationActivity;
import org.Baqqaproject.Baqqa.android.privategroup.invitation.GroupInvitationModule;
import org.Baqqaproject.Baqqa.android.privategroup.list.GroupListFragment;
import org.Baqqaproject.Baqqa.android.privategroup.memberlist.GroupMemberListActivity;
import org.Baqqaproject.Baqqa.android.privategroup.memberlist.GroupMemberModule;
import org.Baqqaproject.Baqqa.android.privategroup.reveal.GroupRevealModule;
import org.Baqqaproject.Baqqa.android.privategroup.reveal.RevealContactsActivity;
import org.Baqqaproject.Baqqa.android.privategroup.reveal.RevealContactsFragment;
import org.Baqqaproject.Baqqa.android.removabledrive.RemovableDriveActivity;
import org.Baqqaproject.Baqqa.android.reporting.CrashFragment;
import org.Baqqaproject.Baqqa.android.reporting.CrashReportActivity;
import org.Baqqaproject.Baqqa.android.reporting.ReportFormFragment;
import org.Baqqaproject.Baqqa.android.settings.ConfirmAvatarDialogFragment;
import org.Baqqaproject.Baqqa.android.settings.SettingsActivity;
import org.Baqqaproject.Baqqa.android.settings.SettingsFragment;
import org.Baqqaproject.Baqqa.android.sharing.BlogInvitationActivity;
import org.Baqqaproject.Baqqa.android.sharing.BlogSharingStatusActivity;
import org.Baqqaproject.Baqqa.android.sharing.ForumInvitationActivity;
import org.Baqqaproject.Baqqa.android.sharing.ForumSharingStatusActivity;
import org.Baqqaproject.Baqqa.android.sharing.ShareBlogActivity;
import org.Baqqaproject.Baqqa.android.sharing.ShareBlogFragment;
import org.Baqqaproject.Baqqa.android.sharing.ShareForumActivity;
import org.Baqqaproject.Baqqa.android.sharing.ShareForumFragment;
import org.Baqqaproject.Baqqa.android.sharing.SharingModule;
import org.Baqqaproject.Baqqa.android.splash.SplashScreenActivity;
import org.Baqqaproject.Baqqa.android.test.TestDataActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {
		ActivityModule.class,
		CreateGroupModule.class,
		GroupInvitationModule.class,
		GroupMemberModule.class,
		GroupRevealModule.class,
		SharingModule.SharingLegacyModule.class
}, dependencies = AndroidComponent.class)
public interface ActivityComponent {

	Activity activity();

	void inject(SplashScreenActivity activity);

	void inject(StartupActivity activity);

	void inject(SetupActivity activity);

	void inject(NavDrawerActivity activity);

	void inject(PanicResponderActivity activity);

	void inject(PanicPreferencesActivity activity);

	void inject(AddNearbyContactActivity activity);

	void inject(ConversationActivity activity);

	void inject(ImageActivity activity);

	void inject(ForumInvitationActivity activity);

	void inject(BlogInvitationActivity activity);

	void inject(CreateGroupActivity activity);

	void inject(GroupActivity activity);

	void inject(GroupInviteActivity activity);

	void inject(GroupInvitationActivity activity);

	void inject(GroupMemberListActivity activity);

	void inject(RevealContactsActivity activity);

	void inject(CreateForumActivity activity);

	void inject(ShareForumActivity activity);

	void inject(ShareBlogActivity activity);

	void inject(ForumSharingStatusActivity activity);

	void inject(BlogSharingStatusActivity activity);

	void inject(ForumActivity activity);

	void inject(BlogActivity activity);

	void inject(WriteBlogPostActivity activity);

	void inject(BlogFragment fragment);

	void inject(BlogPostFragment fragment);

	void inject(ReblogFragment fragment);

	void inject(ReblogActivity activity);

	void inject(SettingsActivity activity);

	void inject(TransportsActivity activity);

	void inject(TestDataActivity activity);

	void inject(ChangePasswordActivity activity);

	void inject(IntroductionActivity activity);

	void inject(RssFeedActivity activity);

	void inject(StartupFailureActivity activity);

	void inject(UnlockActivity activity);

	void inject(AddContactActivity activity);

	void inject(PendingContactListActivity activity);

	void inject(CrashReportActivity crashReportActivity);

	void inject(HotspotActivity hotspotActivity);

	void inject(RemovableDriveActivity activity);

	// Fragments

	void inject(SetupFragment fragment);

	void inject(PasswordFragment imageFragment);

	void inject(OpenDatabaseFragment activity);

	void inject(ContactListFragment fragment);

	void inject(CreateGroupFragment fragment);

	void inject(GroupListFragment fragment);

	void inject(GroupInviteFragment fragment);

	void inject(RevealContactsFragment activity);

	void inject(ForumListFragment fragment);

	void inject(FeedFragment fragment);

	void inject(AddNearbyContactIntroFragment fragment);

	void inject(AddNearbyContactFragment fragment);

	void inject(LinkExchangeFragment fragment);

	void inject(NicknameFragment fragment);

	void inject(ContactChooserFragment fragment);

	void inject(ShareForumFragment fragment);

	void inject(ShareBlogFragment fragment);

	void inject(IntroductionMessageFragment fragment);

	void inject(SettingsFragment fragment);

	void inject(ScreenFilterDialogFragment fragment);

	void inject(AddNearbyContactErrorFragment fragment);

	void inject(AliasDialogFragment aliasDialogFragment);

	void inject(ImageFragment imageFragment);

	void inject(ReportFormFragment reportFormFragment);

	void inject(CrashFragment crashFragment);

	void inject(ConfirmAvatarDialogFragment fragment);

	void inject(ConversationSettingsDialog dialog);

	void inject(RssFeedImportFragment fragment);

	void inject(RssFeedManageFragment fragment);

	void inject(RssFeedImportFailedDialogFragment fragment);

	void inject(RssFeedDeleteFeedDialogFragment fragment);

	void inject(ConnectViaBluetoothActivity connectViaBluetoothActivity);

	void inject(MailboxActivity mailboxActivity);
}



