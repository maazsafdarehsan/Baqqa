package org.Baqqaproject.Baqqa.android.privategroup.reveal;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.contactselection.ContactSelectorController;
import org.Baqqaproject.Baqqa.android.controller.handler.ExceptionHandler;
import org.Baqqaproject.Baqqa.android.controller.handler.ResultExceptionHandler;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;

@NotNullByDefault
public interface RevealContactsController
		extends ContactSelectorController<RevealableContactItem> {

	void isOnboardingNeeded(
			ResultExceptionHandler<Boolean, DbException> handler);

	void onboardingShown(ExceptionHandler<DbException> handler);

	void reveal(GroupId g, Collection<ContactId> contacts,
			ExceptionHandler<DbException> handler);

}




