package org.Baqqaproject.bramble.transport.agreement;

import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface SessionEncoder {

	BdfDictionary encodeSession(Session s, TransportId transportId);

	BdfDictionary getSessionQuery(TransportId transportId);
}




