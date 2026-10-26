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
import org.Baqqaproject.Baqqa.api.blog.BlogSharingManager;
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
class ShareBlogControllerImpl extends ContactSelectorControllerImpl
		implements ShareBlogController {

	private final static Logger LOG =
			getLogger(ShareBlogControllerImpl.class.getName());

	private final BlogSharingManager blogSharingManager;

	@Inject
	ShareBlogControllerImpl(@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager, ContactManager contactManager,
			AuthorManager authorManager,
			BlogSharingManager blogSharingManager) {
		super(dbExecutor, lifecycleManager, contactManager, authorManager);
		this.blogSharingManager = blogSharingManager;
	}

	@Override
	protected SharingStatus getSharingStatus(GroupId g, Contact c)
			throws DbException {
		return blogSharingManager.getSharingStatus(g, c);
	}

	@Override
	public void share(GroupId g, Collection<ContactId> contacts, @Nullable
			String text, ExceptionHandler<DbException> handler) {
		runOnDbThread(() -> {
			try {
				for (ContactId c : contacts) {
					try {
						blogSharingManager.sendInvitation(g, c, text);
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




