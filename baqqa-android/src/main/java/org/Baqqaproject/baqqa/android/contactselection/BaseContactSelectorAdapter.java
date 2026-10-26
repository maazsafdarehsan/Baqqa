package org.Baqqaproject.Baqqa.android.contactselection;

import android.content.Context;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.android.contact.BaseContactListAdapter;
import org.Baqqaproject.Baqqa.android.contact.ContactItemViewHolder;
import org.Baqqaproject.Baqqa.android.contact.OnContactClickListener;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.ArrayList;
import java.util.Collection;

@NotNullByDefault
public abstract class BaseContactSelectorAdapter<I extends BaseSelectableContactItem, H extends ContactItemViewHolder<I>>
		extends BaseContactListAdapter<I, H> {

	public BaseContactSelectorAdapter(Context context, Class<I> c,
			OnContactClickListener<I> listener) {
		super(context, c, listener);
	}

	public Collection<ContactId> getSelectedContactIds() {
		Collection<ContactId> selected = new ArrayList<>();

		for (int i = 0; i < items.size(); i++) {
			BaseSelectableContactItem item = items.get(i);
			if (item.isSelected()) selected.add(item.getContact().getId());
		}
		return selected;
	}

}




