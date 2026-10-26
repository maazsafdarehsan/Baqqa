package org.Baqqaproject.Baqqa.android.privategroup.creation;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.contactselection.ContactSelectorController;
import org.Baqqaproject.Baqqa.android.contactselection.SelectableContactItem;
import org.Baqqaproject.Baqqa.android.controller.handler.ResultExceptionHandler;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;

import androidx.annotation.Nullable;

@NotNullByDefault
public interface CreateGroupController
		extends ContactSelectorController<SelectableContactItem> {

	void createGroup(String name,
			ResultExceptionHandler<GroupId, DbException> result);

	void sendInvitation(GroupId g, Collection<ContactId> contacts,
			@Nullable String text,
			ResultExceptionHandler<Void, DbException> result);

}




