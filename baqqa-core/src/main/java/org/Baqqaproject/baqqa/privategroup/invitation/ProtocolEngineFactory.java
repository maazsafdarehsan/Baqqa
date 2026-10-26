package org.Baqqaproject.Baqqa.privategroup.invitation;

import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface ProtocolEngineFactory {

	ProtocolEngine<CreatorSession> createCreatorEngine();

	ProtocolEngine<InviteeSession> createInviteeEngine();

	ProtocolEngine<PeerSession> createPeerEngine();
}




