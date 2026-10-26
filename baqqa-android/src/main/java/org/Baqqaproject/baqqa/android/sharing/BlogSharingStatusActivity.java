package org.Baqqaproject.Baqqa.android.sharing;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.activity.ActivityComponent;
import org.Baqqaproject.Baqqa.api.blog.BlogInvitationResponse;
import org.Baqqaproject.Baqqa.api.blog.BlogSharingManager;
import org.Baqqaproject.Baqqa.api.blog.event.BlogInvitationResponseReceivedEvent;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import java.util.Collection;

import javax.inject.Inject;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class BlogSharingStatusActivity extends SharingStatusActivity {

	// Fields that are accessed from background threads must be volatile
	@Inject
	protected volatile BlogSharingManager blogSharingManager;

	@Override
	public void injectActivity(ActivityComponent component) {
		component.inject(this);
	}

	@Override
	public void eventOccurred(Event e) {
		super.eventOccurred(e);
		if (e instanceof BlogInvitationResponseReceivedEvent) {
			BlogInvitationResponseReceivedEvent r =
					(BlogInvitationResponseReceivedEvent) e;
			BlogInvitationResponse h = r.getMessageHeader();
			if (h.getShareableId().equals(getGroupId()) && h.wasAccepted()) {
				loadSharedWith();
			}
		}
	}

	@Override
	int getInfoText() {
		return R.string.sharing_status_blog;
	}

	@Override
	@DatabaseExecutor
	protected Collection<Contact> getSharedWith() throws DbException {
		return blogSharingManager.getSharedWith(getGroupId());
	}

}




