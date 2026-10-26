package org.Baqqaproject.Baqqa.api.forum;

import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.Baqqa.api.sharing.SharingManager;

public interface ForumSharingManager extends SharingManager<Forum> {

	/**
	 * The unique ID of the forum sharing client.
	 */
	ClientId CLIENT_ID = new ClientId("org.Baqqaproject.Baqqa.forum.sharing");

	/**
	 * The current major version of the forum sharing client.
	 */
	int MAJOR_VERSION = 0;

	/**
	 * The current minor version of the forum sharing client.
	 */
	int MINOR_VERSION = 1;
}



