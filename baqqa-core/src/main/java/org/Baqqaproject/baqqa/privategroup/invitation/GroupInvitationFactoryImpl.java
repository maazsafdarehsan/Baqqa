package org.Baqqaproject.Baqqa.privategroup.invitation;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.client.ContactGroupFactory;
import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.crypto.PrivateKey;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.bramble.api.identity.AuthorId;
import org.Baqqaproject.bramble.api.sync.Group;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import java.security.GeneralSecurityException;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationManager.CLIENT_ID;
import static org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationManager.MAJOR_VERSION;

@Immutable
@NotNullByDefault
class GroupInvitationFactoryImpl implements GroupInvitationFactory {

	private final ContactGroupFactory contactGroupFactory;
	private final ClientHelper clientHelper;

	@Inject
	GroupInvitationFactoryImpl(ContactGroupFactory contactGroupFactory,
			ClientHelper clientHelper) {
		this.contactGroupFactory = contactGroupFactory;
		this.clientHelper = clientHelper;
	}

	@Override
	public byte[] signInvitation(Contact c, GroupId privateGroupId,
			long timestamp, PrivateKey privateKey) {
		AuthorId creatorId = c.getLocalAuthorId();
		AuthorId memberId = c.getAuthor().getId();
		BdfList token = createInviteToken(creatorId, memberId, privateGroupId,
				timestamp);
		try {
			return clientHelper.sign(SIGNING_LABEL_INVITE, token, privateKey);
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException(e);
		} catch (FormatException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public BdfList createInviteToken(AuthorId creatorId, AuthorId memberId,
			GroupId privateGroupId, long timestamp) {
		Group contactGroup = contactGroupFactory.createContactGroup(CLIENT_ID,
				MAJOR_VERSION, creatorId, memberId);
		return BdfList.of(
				timestamp,
				contactGroup.getId(),
				privateGroupId
		);
	}

}




