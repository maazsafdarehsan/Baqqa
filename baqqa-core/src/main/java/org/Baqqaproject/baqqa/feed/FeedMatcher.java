package org.Baqqaproject.Baqqa.feed;

import org.Baqqaproject.Baqqa.api.feed.Feed;
import org.Baqqaproject.Baqqa.api.feed.RssProperties;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@NotNullByDefault
interface FeedMatcher {

	/**
	 * Returns the best match for the given candidate from the given list of
	 * feeds, or null if there are no matches.
	 */
	@Nullable
	Feed findMatchingFeed(RssProperties candidate, List<Feed> feeds);
}




