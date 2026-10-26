package org.Baqqaproject.Baqqa.privategroup.invitation;

import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface SessionEncoder {

	BdfDictionary encodeSession(Session s);
}




