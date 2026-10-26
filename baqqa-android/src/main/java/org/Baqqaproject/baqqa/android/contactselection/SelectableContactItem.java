package org.Baqqaproject.Baqqa.android.contactselection;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.Baqqaproject.Baqqa.api.sharing.SharingManager.SharingStatus;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.NotThreadSafe;

import static org.Baqqaproject.Baqqa.api.sharing.SharingManager.SharingStatus.SHAREABLE;

@NotThreadSafe
@NotNullByDefault
public class SelectableContactItem extends BaseSelectableContactItem {

	private final SharingStatus sharingStatus;

	public SelectableContactItem(Contact contact, AuthorInfo authorInfo,
			boolean selected, SharingStatus sharingStatus) {
		super(contact, authorInfo, selected);
		this.sharingStatus = sharingStatus;
	}

	public SharingStatus getSharingStatus() {
		return sharingStatus;
	}

	@Override
	public boolean isDisabled() {
		return sharingStatus != SHAREABLE;
	}

}




