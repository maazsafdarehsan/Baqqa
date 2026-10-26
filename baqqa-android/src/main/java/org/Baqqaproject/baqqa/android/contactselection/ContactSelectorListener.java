package org.Baqqaproject.Baqqa.android.contactselection;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;

import androidx.annotation.UiThread;

@NotNullByDefault
public interface ContactSelectorListener {

	@UiThread
	void contactsSelected(Collection<ContactId> contacts);

}




