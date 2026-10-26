package org.Baqqaproject.Baqqa.api.blog;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.bramble.api.sync.Group;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface BlogFactory {

	/**
	 * Creates a personal blog for a given author.
	 */
	Blog createBlog(Author author);

	/**
	 * Creates a RSS feed blog for a given author.
	 */
	Blog createFeedBlog(Author author);

	/**
	 * Parses a blog with the given Group
	 */
	Blog parseBlog(Group g) throws FormatException;

}




