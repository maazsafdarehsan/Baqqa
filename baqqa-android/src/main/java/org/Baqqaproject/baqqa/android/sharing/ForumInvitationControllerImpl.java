package org.Baqqaproject.Baqqa.android.sharing;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.Baqqa.android.controller.handler.ExceptionHandler;
import org.Baqqaproject.Baqqa.api.forum.Forum;
import org.Baqqaproject.Baqqa.api.forum.ForumSharingManager;
import org.Baqqaproject.Baqqa.api.forum.event.ForumInvitationRequestReceivedEvent;
import org.Baqqaproject.Baqqa.api.sharing.SharingInvitationItem;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import static java.util.logging.Level.WARNING;
import static org.Baqqaproject.bramble.util.LogUtils.logException;
import static org.Baqqaproject.Baqqa.api.forum.ForumManager.CLIENT_ID;

@NotNullByDefault
class ForumInvitationControllerImpl
		extends InvitationControllerImpl<SharingInvitationItem>
		implements ForumInvitationController {

	private final ForumSharingManager forumSharingManager;

	@Inject
	ForumInvitationControllerImpl(@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager, EventBus eventBus,
			ForumSharingManager forumSharingManager) {
		super(dbExecutor, lifecycleManager, eventBus);
		this.forumSharingManager = forumSharingManager;
	}

	@Override
	public void eventOccurred(Event e) {
		super.eventOccurred(e);

		if (e instanceof ForumInvitationRequestReceivedEvent) {
			LOG.info("Forum invitation received, reloading");
			listener.loadInvitations(false);
		}
	}

	@Override
	protected ClientId getShareableClientId() {
		return CLIENT_ID;
	}

	@Override
	protected Collection<SharingInvitationItem> getInvitations()
			throws DbException {
		return forumSharingManager.getInvitations();
	}

	@Override
	public void respondToInvitation(SharingInvitationItem item, boolean accept,
			ExceptionHandler<DbException> handler) {
		runOnDbThread(() -> {
			try {
				Forum f = (Forum) item.getShareable();
				for (Contact c : item.getNewSharers()) {
					// TODO: What happens if a contact has been removed?
					forumSharingManager.respondToInvitation(f, c, accept);
				}
			} catch (DbException e) {
				logException(LOG, WARNING, e);
				handler.onException(e);
			}
		});
	}

}




