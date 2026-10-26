package org.Baqqaproject.Baqqa.api.blog;

import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.forum.ForumPost;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class BlogPost extends ForumPost {

	public BlogPost(Message message, @Nullable MessageId parent,
			Author author) {
		super(message, parent, author);
	}
}




