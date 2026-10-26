package org.Baqqaproject.Baqqa.android.sharing;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.Baqqa.android.controller.handler.ExceptionHandler;
import org.Baqqaproject.Baqqa.api.blog.Blog;
import org.Baqqaproject.Baqqa.api.blog.BlogSharingManager;
import org.Baqqaproject.Baqqa.api.blog.event.BlogInvitationRequestReceivedEvent;
import org.Baqqaproject.Baqqa.api.sharing.SharingInvitationItem;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import static java.util.logging.Level.WARNING;
import static org.Baqqaproject.bramble.util.LogUtils.logException;
import static org.Baqqaproject.Baqqa.api.blog.BlogManager.CLIENT_ID;

@NotNullByDefault
class BlogInvitationControllerImpl
		extends InvitationControllerImpl<SharingInvitationItem>
		implements BlogInvitationController {

	private final BlogSharingManager blogSharingManager;

	@Inject
	BlogInvitationControllerImpl(@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager, EventBus eventBus,
			BlogSharingManager blogSharingManager) {
		super(dbExecutor, lifecycleManager, eventBus);
		this.blogSharingManager = blogSharingManager;
	}

	@Override
	public void eventOccurred(Event e) {
		super.eventOccurred(e);

		if (e instanceof BlogInvitationRequestReceivedEvent) {
			LOG.info("Blog invitation received, reloading");
			listener.loadInvitations(false);
		}
	}

	@Override
	protected ClientId getShareableClientId() {
		return CLIENT_ID;
	}

	@Override
	protected Collection<SharingInvitationItem> getInvitations() throws DbException {
		return blogSharingManager.getInvitations();
	}

	@Override
	public void respondToInvitation(SharingInvitationItem item, boolean accept,
			ExceptionHandler<DbException> handler) {
		runOnDbThread(() -> {
			try {
				Blog f = (Blog) item.getShareable();
				for (Contact c : item.getNewSharers()) {
					// TODO: What happens if a contact has been removed?
					blogSharingManager.respondToInvitation(f, c, accept);
				}
			} catch (DbException e) {
				logException(LOG, WARNING, e);
				handler.onException(e);
			}
		});
	}

}




