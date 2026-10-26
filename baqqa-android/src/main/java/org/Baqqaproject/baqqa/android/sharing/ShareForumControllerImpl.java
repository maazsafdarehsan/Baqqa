package org.Baqqaproject.Baqqa.android.sharing;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.NoSuchContactException;
import org.Baqqaproject.bramble.api.db.NoSuchGroupException;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.contactselection.ContactSelectorControllerImpl;
import org.Baqqaproject.Baqqa.android.controller.handler.ExceptionHandler;
import org.Baqqaproject.Baqqa.api.forum.ForumSharingManager;
import org.Baqqaproject.Baqqa.api.identity.AuthorManager;
import org.Baqqaproject.Baqqa.api.sharing.SharingManager.SharingStatus;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static org.Baqqaproject.bramble.util.LogUtils.logException;

@Immutable
@NotNullByDefault
class ShareForumControllerImpl extends ContactSelectorControllerImpl
		implements ShareForumController {

	private final static Logger LOG =
			getLogger(ShareForumControllerImpl.class.getName());

	private final ForumSharingManager forumSharingManager;

	@Inject
	ShareForumControllerImpl(@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager, ContactManager contactManager,
			AuthorManager authorManager,
			ForumSharingManager forumSharingManager) {
		super(dbExecutor, lifecycleManager, contactManager, authorManager);
		this.forumSharingManager = forumSharingManager;
	}

	@Override
	protected SharingStatus getSharingStatus(GroupId g, Contact c)
			throws DbException {
		return forumSharingManager.getSharingStatus(g, c);
	}

	@Override
	public void share(GroupId g, Collection<ContactId> contacts,
			@Nullable String text, ExceptionHandler<DbException> handler) {
		runOnDbThread(() -> {
			try {
				for (ContactId c : contacts) {
					try {
						forumSharingManager.sendInvitation(g, c, text);
					} catch (NoSuchContactException | NoSuchGroupException e) {
						logException(LOG, WARNING, e);
					}
				}
			} catch (DbException e) {
				logException(LOG, WARNING, e);
				handler.onException(e);
			}
		});
	}

}




