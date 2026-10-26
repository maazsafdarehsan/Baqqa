package org.Baqqaproject.bramble.transport.agreement;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface SessionParser {

	Session parseSession(BdfDictionary meta) throws FormatException;
}




