package org.Baqqaproject.Baqqa.android.settings;

import org.Baqqaproject.bramble.api.identity.LocalAuthor;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
class OwnIdentityInfo {

	private final LocalAuthor localAuthor;
	private final AuthorInfo authorInfo;

	OwnIdentityInfo(LocalAuthor localAuthor, AuthorInfo authorInfo) {
		this.localAuthor = localAuthor;
		this.authorInfo = authorInfo;
	}

	LocalAuthor getLocalAuthor() {
		return localAuthor;
	}

	AuthorInfo getAuthorInfo() {
		return authorInfo;
	}

}



