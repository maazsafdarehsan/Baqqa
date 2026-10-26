package org.Baqqaproject.Baqqa.android.sharing;

import android.content.Context;

import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.activity.ActivityComponent;
import org.Baqqaproject.Baqqa.api.sharing.SharingInvitationItem;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import javax.inject.Inject;

import static org.Baqqaproject.Baqqa.android.sharing.InvitationAdapter.InvitationClickListener;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class ForumInvitationActivity
		extends InvitationActivity<SharingInvitationItem> {

	@Inject
	ForumInvitationController controller;

	@Override
	public void injectActivity(ActivityComponent component) {
		component.inject(this);
	}

	@Override
	protected InvitationController<SharingInvitationItem> getController() {
		return controller;
	}

	@Override
	protected InvitationAdapter<SharingInvitationItem, ?> getAdapter(
			Context ctx,
			InvitationClickListener<SharingInvitationItem> listener) {
		return new SharingInvitationAdapter(ctx, listener);
	}

	@Override
	protected int getAcceptRes() {
		return R.string.forum_joined_toast;
	}

	@Override
	protected int getDeclineRes() {
		return R.string.forum_declined_toast;
	}

}




