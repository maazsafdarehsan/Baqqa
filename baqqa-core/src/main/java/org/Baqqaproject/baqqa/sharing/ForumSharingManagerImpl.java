package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.client.ContactGroupFactory;
import org.Baqqaproject.bramble.api.data.MetadataParser;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.bramble.api.versioning.ClientVersioningManager;
import org.Baqqaproject.Baqqa.api.client.MessageTracker;
import org.Baqqaproject.Baqqa.api.forum.Forum;
import org.Baqqaproject.Baqqa.api.forum.ForumInvitationResponse;
import org.Baqqaproject.Baqqa.api.forum.ForumManager;
import org.Baqqaproject.Baqqa.api.forum.ForumManager.RemoveForumHook;
import org.Baqqaproject.Baqqa.api.forum.ForumSharingManager;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.inject.Inject;

@NotNullByDefault
class ForumSharingManagerImpl extends SharingManagerImpl<Forum>
		implements ForumSharingManager, RemoveForumHook {

	@Inject
	ForumSharingManagerImpl(DatabaseComponent db, ClientHelper clientHelper,
			ClientVersioningManager clientVersioningManager,
			MetadataParser metadataParser, MessageParser<Forum> messageParser,
			SessionEncoder sessionEncoder, SessionParser sessionParser,
			MessageTracker messageTracker,
			ContactGroupFactory contactGroupFactory,
			ProtocolEngine<Forum> engine,
			InvitationFactory<Forum, ForumInvitationResponse> invitationFactory) {
		super(db, clientHelper, clientVersioningManager, metadataParser,
				messageParser, sessionEncoder, sessionParser, messageTracker,
				contactGroupFactory, engine, invitationFactory);
	}

	@Override
	protected ClientId getClientId() {
		return CLIENT_ID;
	}

	@Override
	protected int getMajorVersion() {
		return MAJOR_VERSION;
	}

	@Override
	protected ClientId getShareableClientId() {
		return ForumManager.CLIENT_ID;
	}

	@Override
	protected int getShareableMajorVersion() {
		return ForumManager.MAJOR_VERSION;
	}

	@Override
	public void removingForum(Transaction txn, Forum f) throws DbException {
		removingShareable(txn, f);
	}

}




