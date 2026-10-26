package org.Baqqaproject.Baqqa.api.forum;

import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.client.PostHeader;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class ForumPostHeader extends PostHeader {

	public ForumPostHeader(MessageId id, @Nullable MessageId parentId,
			long timestamp, Author author, AuthorInfo authorInfo,
			boolean read) {
		super(id, parentId, timestamp, author, authorInfo, read);
	}

}




