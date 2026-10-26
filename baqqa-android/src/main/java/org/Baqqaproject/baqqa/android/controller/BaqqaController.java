package org.Baqqaproject.Baqqa.android.controller;

import org.Baqqaproject.bramble.api.system.Wakeful;
import org.Baqqaproject.Baqqa.android.controller.handler.ResultHandler;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface BaqqaController extends ActivityLifecycleController {

	void startAndBindService();

	boolean accountSignedIn();

	/**
	 * Returns true via the handler when the app has dozed
	 * without being white-listed.
	 */
	void hasDozed(ResultHandler<Boolean> handler);

	void doNotAskAgainForDozeWhiteListing();

	@Wakeful
	void signOut(ResultHandler<Void> handler, boolean deleteAccount);

	void deleteAccount();

}




