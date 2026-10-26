package org.Baqqaproject.Baqqa.android.forum;

import org.Baqqaproject.Baqqa.android.threaded.ThreadItem;
import org.Baqqaproject.Baqqa.api.forum.ForumPostHeader;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
class ForumPostItem extends ThreadItem {

	ForumPostItem(ForumPostHeader h, String text) {
		super(h.getId(), h.getParentId(), text, h.getTimestamp(), h.getAuthor(),
				h.getAuthorInfo(), h.isRead());
	}

}



