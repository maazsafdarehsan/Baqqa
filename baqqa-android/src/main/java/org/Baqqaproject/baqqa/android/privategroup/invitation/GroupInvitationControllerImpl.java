package org.Baqqaproject.Baqqa.android.privategroup.invitation;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.Baqqa.android.controller.handler.ExceptionHandler;
import org.Baqqaproject.Baqqa.android.sharing.InvitationControllerImpl;
import org.Baqqaproject.Baqqa.api.privategroup.PrivateGroup;
import org.Baqqaproject.Baqqa.api.privategroup.event.GroupInvitationRequestReceivedEvent;
import org.Baqqaproject.Baqqa.api.privategroup.event.GroupInvitationResponseReceivedEvent;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationItem;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationManager;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import static java.util.logging.Level.WARNING;
import static org.Baqqaproject.bramble.util.LogUtils.logException;
import static org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupManager.CLIENT_ID;

@NotNullByDefault
class GroupInvitationControllerImpl
		extends InvitationControllerImpl<GroupInvitationItem>
		implements GroupInvitationController {

	private final GroupInvitationManager groupInvitationManager;

	@Inject
	GroupInvitationControllerImpl(@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager, EventBus eventBus,
			GroupInvitationManager groupInvitationManager) {
		super(dbExecutor, lifecycleManager, eventBus);
		this.groupInvitationManager = groupInvitationManager;
	}

	@Override
	public void eventOccurred(Event e) {
		super.eventOccurred(e);

		if (e instanceof GroupInvitationRequestReceivedEvent) {
			LOG.info("Group invitation request received, reloading");
			listener.loadInvitations(false);
		} else if (e instanceof GroupInvitationResponseReceivedEvent) {
			LOG.info("Group invitation response received, reloading");
			listener.loadInvitations(false);
		}
	}

	@Override
	protected ClientId getShareableClientId() {
		return CLIENT_ID;
	}

	@Override
	protected Collection<GroupInvitationItem> getInvitations()
			throws DbException {
		return groupInvitationManager.getInvitations();
	}

	@Override
	public void respondToInvitation(GroupInvitationItem item, boolean accept,
			ExceptionHandler<DbException> handler) {
		runOnDbThread(() -> {
			try {
				PrivateGroup g = item.getShareable();
				ContactId c = item.getCreator().getId();
				groupInvitationManager.respondToInvitation(c, g, accept);
			} catch (DbException e) {
				logException(LOG, WARNING, e);
				handler.onException(e);
			}
		});
	}

}




