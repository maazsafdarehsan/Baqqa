package org.Baqqaproject.Baqqa.api.attachment;

import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class AttachmentHeader {

	private final GroupId groupId;
	private final MessageId messageId;
	private final String contentType;
	@Nullable
	private final String fileName;
	private final long totalSize;

	public AttachmentHeader(GroupId groupId, MessageId messageId,
			String contentType) {
		this(groupId, messageId, contentType, null, -1);
	}

	public AttachmentHeader(GroupId groupId, MessageId messageId,
			String contentType, @Nullable String fileName, long totalSize) {
		this.groupId = groupId;
		this.messageId = messageId;
		this.contentType = contentType;
		this.fileName = fileName;
		this.totalSize = totalSize;
	}

	public GroupId getGroupId() {
		return groupId;
	}

	public MessageId getMessageId() {
		return messageId;
	}

	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns the original file name of the attachment, if known,
	 * or null.
	 */
	@Nullable
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns the total size of the attachment's data in bytes, or -1 if
	 * the size is not known.
	 */
	public long getTotalSize() {
		return totalSize;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AttachmentHeader) {
			AttachmentHeader h = (AttachmentHeader) o;
			return groupId.equals(h.groupId) && messageId.equals(h.messageId);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return messageId.hashCode();
	}
}




