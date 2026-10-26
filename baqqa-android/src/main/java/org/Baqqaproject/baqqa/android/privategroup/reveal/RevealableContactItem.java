package org.Baqqaproject.Baqqa.android.privategroup.reveal;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.Baqqa.android.contactselection.BaseSelectableContactItem;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.Baqqaproject.Baqqa.api.privategroup.Visibility;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.NotThreadSafe;

import static org.Baqqaproject.Baqqa.api.privategroup.Visibility.INVISIBLE;

@NotThreadSafe
@NotNullByDefault
class RevealableContactItem extends BaseSelectableContactItem {

	private final Visibility visibility;

	RevealableContactItem(Contact contact, AuthorInfo authorInfo,
			boolean selected, Visibility visibility) {
		super(contact, authorInfo, selected);
		this.visibility = visibility;
	}

	Visibility getVisibility() {
		return visibility;
	}

	@Override
	public boolean isDisabled() {
		return visibility != INVISIBLE;
	}
}




