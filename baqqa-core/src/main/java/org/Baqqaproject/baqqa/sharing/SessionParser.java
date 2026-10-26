package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface SessionParser {

	BdfDictionary getSessionQuery(SessionId s);

	BdfDictionary getAllSessionsQuery();

	boolean isSession(BdfDictionary d) throws FormatException;

	Session parseSession(GroupId contactGroupId, BdfDictionary d)
			throws FormatException;

}




