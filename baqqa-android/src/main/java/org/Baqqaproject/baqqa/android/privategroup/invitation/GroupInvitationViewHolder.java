package org.Baqqaproject.Baqqa.android.privategroup.invitation;

import android.view.View;

import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.sharing.InvitationAdapter.InvitationClickListener;
import org.Baqqaproject.Baqqa.android.sharing.InvitationViewHolder;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationItem;

import javax.annotation.Nullable;

import static org.Baqqaproject.Baqqa.android.util.UiUtils.getContactDisplayName;

class GroupInvitationViewHolder
		extends InvitationViewHolder<GroupInvitationItem> {

	GroupInvitationViewHolder(View v) {
		super(v);
	}

	@Override
	public void onBind(@Nullable GroupInvitationItem item,
			InvitationClickListener<GroupInvitationItem> listener) {
		super.onBind(item, listener);
		if (item == null) return;

		sharedBy.setText(
				sharedBy.getContext().getString(R.string.groups_created_by,
						getContactDisplayName(item.getCreator())));
	}

}


