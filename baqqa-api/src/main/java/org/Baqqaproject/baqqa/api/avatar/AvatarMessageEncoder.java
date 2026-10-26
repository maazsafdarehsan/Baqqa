package org.Baqqaproject.Baqqa.api.avatar;

import org.Baqqaproject.bramble.api.Pair;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.Message;

import java.io.IOException;
import java.io.InputStream;

public interface AvatarMessageEncoder {
	/**
	 * Returns an update message and its metadata.
	 */
	Pair<Message, BdfDictionary> encodeUpdateMessage(GroupId groupId,
			long version, String contentType, InputStream in)
			throws IOException;
}



