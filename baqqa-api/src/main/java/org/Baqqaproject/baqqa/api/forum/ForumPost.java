package org.Baqqaproject.Baqqa.api.forum;

import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.client.ThreadedMessage;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class ForumPost extends ThreadedMessage {

	public ForumPost(Message message, @Nullable MessageId parent,
			Author author) {
		super(message, parent, author);
	}

}




