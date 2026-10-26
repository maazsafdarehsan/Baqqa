package org.Baqqaproject.Baqqa.android.contactselection;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.controller.DbController;
import org.Baqqaproject.Baqqa.android.controller.handler.ResultExceptionHandler;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;

@NotNullByDefault
public interface ContactSelectorController<I extends BaseSelectableContactItem>
		extends DbController {

	void loadContacts(GroupId g, Collection<ContactId> selection,
			ResultExceptionHandler<Collection<I>, DbException> handler);

}




