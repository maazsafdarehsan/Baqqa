package org.Baqqaproject.Baqqa.android.contactselection;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.controller.DbControllerImpl;
import org.Baqqaproject.Baqqa.android.controller.handler.ResultExceptionHandler;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.Baqqaproject.Baqqa.api.identity.AuthorManager;
import org.Baqqaproject.Baqqa.api.sharing.SharingManager.SharingStatus;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.annotation.concurrent.Immutable;

import static java.util.logging.Level.WARNING;
import static org.Baqqaproject.bramble.util.LogUtils.logException;

@Immutable
@NotNullByDefault
public abstract class ContactSelectorControllerImpl
		extends DbControllerImpl
		implements ContactSelectorController<SelectableContactItem> {

	private static final Logger LOG =
			Logger.getLogger(ContactSelectorControllerImpl.class.getName());

	private final ContactManager contactManager;
	private final AuthorManager authorManager;

	public ContactSelectorControllerImpl(@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager, ContactManager contactManager,
			AuthorManager authorManager) {
		super(dbExecutor, lifecycleManager);
		this.contactManager = contactManager;
		this.authorManager = authorManager;
	}

	@Override
	public void loadContacts(GroupId g, Collection<ContactId> selection,
			ResultExceptionHandler<Collection<SelectableContactItem>, DbException> handler) {
		runOnDbThread(() -> {
			try {
				Collection<SelectableContactItem> contacts = new ArrayList<>();
				for (Contact c : contactManager.getContacts()) {
					AuthorInfo authorInfo = authorManager.getAuthorInfo(c);
					// was this contact already selected?
					boolean selected = selection.contains(c.getId());
					contacts.add(new SelectableContactItem(c, authorInfo,
							selected, getSharingStatus(g, c)));
				}
				handler.onResult(contacts);
			} catch (DbException e) {
				logException(LOG, WARNING, e);
				handler.onException(e);
			}
		});
	}

	@DatabaseExecutor
	protected abstract SharingStatus getSharingStatus(GroupId g, Contact c)
			throws DbException;

}




