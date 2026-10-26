package org.Baqqaproject.Baqqa.privategroup.invitation;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface SessionParser {

	BdfDictionary getSessionQuery(SessionId s);

	BdfDictionary getAllSessionsQuery();

	Role getRole(BdfDictionary d) throws FormatException;

	boolean isSession(BdfDictionary d) throws FormatException;

	Session parseSession(GroupId contactGroupId, BdfDictionary d)
			throws FormatException;

	CreatorSession parseCreatorSession(GroupId contactGroupId, BdfDictionary d)
			throws FormatException;

	InviteeSession parseInviteeSession(GroupId contactGroupId, BdfDictionary d)
			throws FormatException;

	PeerSession parsePeerSession(GroupId contactGroupId, BdfDictionary d)
			throws FormatException;
}




