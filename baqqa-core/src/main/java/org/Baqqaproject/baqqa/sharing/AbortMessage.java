package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
class AbortMessage extends SharingMessage {

	AbortMessage(MessageId id, GroupId contactGroupId, GroupId shareableId,
			long timestamp, @Nullable MessageId previousMessageId) {
		super(id, contactGroupId, shareableId, timestamp, previousMessageId);
	}

}




