package org.Baqqaproject.bramble.transport.agreement;

import org.Baqqaproject.bramble.api.crypto.PublicKey;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface MessageEncoder {

	Message encodeKeyMessage(GroupId contactGroupId,
			TransportId transportId, PublicKey publicKey);

	Message encodeActivateMessage(GroupId contactGroupId,
			TransportId transportId, MessageId previousMessageId);

	BdfDictionary encodeMessageMetadata(TransportId transportId,
			MessageType type, boolean local);
}




