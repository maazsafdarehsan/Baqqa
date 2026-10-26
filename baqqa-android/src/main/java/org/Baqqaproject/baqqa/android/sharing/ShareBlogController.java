package org.Baqqaproject.Baqqa.android.sharing;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.contactselection.ContactSelectorController;
import org.Baqqaproject.Baqqa.android.contactselection.SelectableContactItem;
import org.Baqqaproject.Baqqa.android.controller.handler.ExceptionHandler;

import java.util.Collection;

import javax.annotation.Nullable;

public interface ShareBlogController
		extends ContactSelectorController<SelectableContactItem> {

	void share(GroupId g, Collection<ContactId> contacts, @Nullable String text,
			ExceptionHandler<DbException> handler);

}



