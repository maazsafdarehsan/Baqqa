package org.Baqqaproject.Baqqa.android.sharing;

import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.Baqqa.android.controller.ActivityLifecycleController;
import org.Baqqaproject.Baqqa.android.controller.handler.ExceptionHandler;
import org.Baqqaproject.Baqqa.android.controller.handler.ResultExceptionHandler;
import org.Baqqaproject.Baqqa.api.sharing.InvitationItem;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;

@NotNullByDefault
public interface InvitationController<I extends InvitationItem>
		extends ActivityLifecycleController {

	void loadInvitations(boolean clear,
			ResultExceptionHandler<Collection<I>, DbException> handler);

	void respondToInvitation(I item, boolean accept,
			ExceptionHandler<DbException> handler);

	interface InvitationListener {

		void loadInvitations(boolean clear);

	}

}




