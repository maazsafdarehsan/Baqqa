package org.Baqqaproject.Baqqa.api.blog;

import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static org.Baqqaproject.Baqqa.api.blog.MessageType.COMMENT;
import static org.Baqqaproject.Baqqa.api.blog.MessageType.WRAPPED_COMMENT;

@Immutable
@NotNullByDefault
public class BlogCommentHeader extends BlogPostHeader {

	@Nullable
	private final String comment;
	private final BlogPostHeader parent;

	public BlogCommentHeader(MessageType type, GroupId groupId,
			@Nullable String comment, BlogPostHeader parent, MessageId id,
			long timestamp, long timeReceived, Author author,
			AuthorInfo authorInfo, boolean read) {

		super(type, groupId, id, parent.getId(), timestamp,
				timeReceived, author, authorInfo, false, read);

		if (type != COMMENT && type != WRAPPED_COMMENT)
			throw new IllegalArgumentException("Incompatible Message Type");

		this.comment = comment;
		this.parent = parent;
	}

	@Nullable
	public String getComment() {
		return comment;
	}

	public BlogPostHeader getParent() {
		return parent;
	}

	public BlogPostHeader getRootPost() {
		if (parent instanceof BlogCommentHeader)
			return ((BlogCommentHeader) parent).getRootPost();
		return parent;
	}

}




