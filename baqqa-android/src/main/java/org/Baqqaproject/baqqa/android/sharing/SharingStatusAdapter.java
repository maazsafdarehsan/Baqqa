package org.Baqqaproject.Baqqa.android.sharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.contact.BaseContactListAdapter;
import org.Baqqaproject.Baqqa.android.contact.ContactItem;
import org.Baqqaproject.Baqqa.android.contact.ContactItemViewHolder;

class SharingStatusAdapter extends
		BaseContactListAdapter<ContactItem, ContactItemViewHolder<ContactItem>> {

	SharingStatusAdapter(Context context) {
		super(context, ContactItem.class, null);
	}

	@Override
	public ContactItemViewHolder<ContactItem> onCreateViewHolder(
			ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.list_item_contact_small, viewGroup, false);
		return new ContactItemViewHolder<>(v);
	}

}



