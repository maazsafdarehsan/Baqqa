package org.Baqqaproject.Baqqa.introduction;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.Baqqaproject.Baqqa.api.introduction.Role;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface SessionParser {

	BdfDictionary getSessionQuery(SessionId s);

	Role getRole(BdfDictionary d) throws FormatException;

	IntroducerSession parseIntroducerSession(BdfDictionary d)
			throws FormatException;

	IntroduceeSession parseIntroduceeSession(GroupId introducerGroupId,
			BdfDictionary d) throws FormatException;

}




