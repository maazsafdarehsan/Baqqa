package org.Baqqaproject.Baqqa.android.contact;

import android.app.Application;

import org.Baqqaproject.bramble.api.connection.ConnectionRegistry;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.contact.event.PendingContactAddedEvent;
import org.Baqqaproject.bramble.api.contact.event.PendingContactRemovedEvent;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.TransactionManager;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.system.AndroidExecutor;
import org.Baqqaproject.Baqqa.api.android.AndroidNotificationManager;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager;
import org.Baqqaproject.Baqqa.api.identity.AuthorManager;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@NotNullByDefault
class ContactListViewModel extends ContactsViewModel {

	private final AndroidNotificationManager notificationManager;

	private final MutableLiveData<Boolean> hasPendingContacts =
			new MutableLiveData<>();

	@Inject
	ContactListViewModel(Application application,
			@DatabaseExecutor Executor dbExecutor,
			LifecycleManager lifecycleManager, TransactionManager db,
			AndroidExecutor androidExecutor, ContactManager contactManager,
			AuthorManager authorManager,
			ConversationManager conversationManager,
			ConnectionRegistry connectionRegistry, EventBus eventBus,
			AndroidNotificationManager notificationManager) {
		super(application, dbExecutor, lifecycleManager, db, androidExecutor,
				contactManager, authorManager, conversationManager,
				connectionRegistry, eventBus);
		this.notificationManager = notificationManager;
	}

	@Override
	public void eventOccurred(Event e) {
		super.eventOccurred(e);
		if (e instanceof PendingContactAddedEvent ||
				e instanceof PendingContactRemovedEvent) {
			checkForPendingContacts();
		}
	}

	LiveData<Boolean> getHasPendingContacts() {
		return hasPendingContacts;
	}

	void checkForPendingContacts() {
		runOnDbThread(() -> {
			try {
				boolean hasPending =
						!contactManager.getPendingContacts().isEmpty();
				hasPendingContacts.postValue(hasPending);
			} catch (DbException e) {
				handleException(e);
			}
		});
	}

	void clearAllContactNotifications() {
		notificationManager.clearAllContactNotifications();
	}

	void clearAllContactAddedNotifications() {
		notificationManager.clearAllContactAddedNotifications();
	}

}




