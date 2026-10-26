package org.Baqqaproject.Baqqa.android.privategroup.conversation;

import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.threaded.ThreadItem;
import org.Baqqaproject.Baqqa.api.privategroup.GroupMessageHeader;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import androidx.annotation.LayoutRes;
import androidx.annotation.UiThread;

@UiThread
@NotThreadSafe
class GroupMessageItem extends ThreadItem {

	private final GroupId groupId;

	private GroupMessageItem(MessageId messageId, GroupId groupId,
			@Nullable MessageId parentId, String text, long timestamp,
			Author author, AuthorInfo authorInfo, boolean isRead) {
		super(messageId, parentId, text, timestamp, author, authorInfo, isRead);
		this.groupId = groupId;
	}

	GroupMessageItem(GroupMessageHeader h, String text) {
		this(h.getId(), h.getGroupId(), h.getParentId(), text, h.getTimestamp(),
				h.getAuthor(), h.getAuthorInfo(), h.isRead());
	}

	public GroupId getGroupId() {
		return groupId;
	}

	@LayoutRes
	public int getLayout() {
		return R.layout.list_item_thread;
	}

}



