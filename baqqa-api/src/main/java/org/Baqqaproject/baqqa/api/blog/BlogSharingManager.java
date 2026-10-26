package org.Baqqaproject.Baqqa.api.blog;

import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.Baqqa.api.sharing.SharingManager;

public interface BlogSharingManager extends SharingManager<Blog> {

	/**
	 * The unique ID of the blog sharing client.
	 */
	ClientId CLIENT_ID = new ClientId("org.Baqqaproject.Baqqa.blog.sharing");

	/**
	 * The current major version of the blog sharing client.
	 */
	int MAJOR_VERSION = 0;

	/**
	 * The current minor version of the blog sharing client.
	 */
	int MINOR_VERSION = 1;
}



