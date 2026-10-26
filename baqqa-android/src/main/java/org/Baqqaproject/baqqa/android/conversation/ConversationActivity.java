package org.Baqqaproject.Baqqa.android.conversation;

import android.annotation.SuppressLint;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.transition.Slide;
import android.transition.Transition;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.Baqqaproject.bramble.api.FeatureFlags;
import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.Pair;
import org.Baqqaproject.bramble.api.connection.ConnectionRegistry;
import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.contact.event.ContactRemovedEvent;
import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.NoSuchContactException;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.event.EventListener;
import org.Baqqaproject.bramble.api.plugin.event.ContactConnectedEvent;
import org.Baqqaproject.bramble.api.plugin.event.ContactDisconnectedEvent;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.bramble.api.sync.event.MessagesAckedEvent;
import org.Baqqaproject.bramble.api.sync.event.MessagesSentEvent;
import org.Baqqaproject.bramble.api.versioning.event.ClientVersionUpdatedEvent;
import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.activity.ActivityComponent;
import org.Baqqaproject.Baqqa.android.activity.BaqqaActivity;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentItem;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentRetriever;
import org.Baqqaproject.Baqqa.android.blog.BlogActivity;
import org.Baqqaproject.Baqqa.android.contact.connect.ConnectViaBluetoothActivity;
import org.Baqqaproject.Baqqa.android.conversation.ConversationVisitor.AttachmentCache;
import org.Baqqaproject.Baqqa.android.conversation.ConversationVisitor.TextCache;
import org.Baqqaproject.Baqqa.android.forum.ForumActivity;
import org.Baqqaproject.Baqqa.android.fragment.BaseFragment.BaseFragmentListener;
import org.Baqqaproject.Baqqa.android.introduction.IntroductionActivity;
import org.Baqqaproject.Baqqa.android.privategroup.conversation.GroupActivity;
import org.Baqqaproject.Baqqa.android.removabledrive.RemovableDriveActivity;
import org.Baqqaproject.Baqqa.android.util.ActivityLaunchers.GetMultipleFilesAdvanced;
import org.Baqqaproject.Baqqa.android.util.UiUtils;
import org.Baqqaproject.Baqqa.android.util.ActivityLaunchers.GetMultipleImagesAdvanced;
import org.Baqqaproject.Baqqa.android.util.ActivityLaunchers.OpenMultipleFilesAdvanced;
import org.Baqqaproject.Baqqa.android.util.ActivityLaunchers.OpenMultipleImageDocumentsAdvanced;
import org.Baqqaproject.Baqqa.android.util.BaqqaSnackbarBuilder;
import org.Baqqaproject.Baqqa.android.view.BaqqaRecyclerView;
import org.Baqqaproject.Baqqa.android.view.ImagePreview;
import org.Baqqaproject.Baqqa.android.view.TextAttachmentController;
import org.Baqqaproject.Baqqa.android.view.TextAttachmentController.AttachmentListener;
import org.Baqqaproject.Baqqa.android.view.TextInputView;
import org.Baqqaproject.Baqqa.android.view.TextSendController;
import org.Baqqaproject.Baqqa.android.view.TextSendController.SendState;
import org.Baqqaproject.Baqqa.android.voice.AudioFileProvider;
import org.Baqqaproject.Baqqa.android.voice.AudioPlayer;
import org.Baqqaproject.Baqqa.android.voice.VoiceNote;
import org.Baqqaproject.Baqqa.android.voice.VoiceNoteRecorder;
import org.Baqqaproject.Baqqa.android.widget.LinkDialogFragment;
import org.Baqqaproject.Baqqa.api.android.AndroidNotificationManager;
import org.Baqqaproject.Baqqa.api.attachment.Attachment;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentReader;
import org.Baqqaproject.Baqqa.api.autodelete.event.ConversationMessagesDeletedEvent;
import org.Baqqaproject.Baqqa.api.blog.BlogSharingManager;
import org.Baqqaproject.Baqqa.api.client.ProtocolStateException;
import org.Baqqaproject.Baqqa.api.client.SessionId;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager;
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageHeader;
import org.Baqqaproject.Baqqa.api.conversation.ConversationMessageVisitor;
import org.Baqqaproject.Baqqa.api.conversation.ConversationRequest;
import org.Baqqaproject.Baqqa.api.conversation.ConversationResponse;
import org.Baqqaproject.Baqqa.api.conversation.DeletionResult;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.Baqqaproject.Baqqa.api.forum.ForumSharingManager;
import org.Baqqaproject.Baqqa.api.introduction.IntroductionManager;
import org.Baqqaproject.Baqqa.api.messaging.MessagingManager;
import org.Baqqaproject.Baqqa.api.messaging.PrivateMessageHeader;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationManager;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.inject.Inject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.Gravity.RIGHT;
import static android.widget.Toast.LENGTH_SHORT;
import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;
import static androidx.lifecycle.Lifecycle.State.STARTED;
import static androidx.recyclerview.widget.SortedList.INVALID_POSITION;
import static java.util.Collections.singletonList;
import static java.util.Collections.sort;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static org.Baqqaproject.bramble.util.LogUtils.logDuration;
import static org.Baqqaproject.bramble.util.LogUtils.logException;
import static org.Baqqaproject.bramble.util.LogUtils.now;
import static org.Baqqaproject.bramble.util.StringUtils.fromHexString;
import static org.Baqqaproject.bramble.util.StringUtils.isNullOrEmpty;
import static org.Baqqaproject.bramble.util.StringUtils.join;
import static org.Baqqaproject.Baqqa.android.activity.RequestCodes.REQUEST_INTRODUCTION;
import static org.Baqqaproject.Baqqa.android.conversation.ImageActivity.ATTACHMENTS;
import static org.Baqqaproject.Baqqa.android.conversation.ImageActivity.ATTACHMENT_POSITION;
import static org.Baqqaproject.Baqqa.android.conversation.ImageActivity.DATE;
import static org.Baqqaproject.Baqqa.android.conversation.ImageActivity.ITEM_ID;
import static org.Baqqaproject.Baqqa.android.conversation.ImageActivity.NAME;
import static org.Baqqaproject.Baqqa.android.util.UiUtils.launchActivityToOpenFile;
import static org.Baqqaproject.Baqqa.android.util.UiUtils.observeOnce;
import static org.Baqqaproject.Baqqa.android.view.AuthorView.setAvatar;
import static org.Baqqaproject.Baqqa.api.messaging.MessagingConstants.MAX_ATTACHMENTS_PER_MESSAGE;
import static org.Baqqaproject.Baqqa.api.messaging.MessagingConstants.MAX_PRIVATE_MESSAGE_TEXT_LENGTH;
import static org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFormat.TEXT_IMAGES_AUTO_DELETE;
import static org.Baqqaproject.Baqqa.api.messaging.PrivateMessageFormat.TEXT_ONLY;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class ConversationActivity extends BaqqaActivity
		implements BaseFragmentListener, EventListener, ConversationListener,
		TextCache, AttachmentCache, AttachmentListener, ActionMode.Callback {

	public static final String CONTACT_ID = "Baqqa.CONTACT_ID";

	private static final Logger LOG =
			getLogger(ConversationActivity.class.getName());

	private static final int TRANSITION_DURATION_MS = 500;
	private static final int ONBOARDING_DELAY_MS = 250;

	@Inject
	AndroidNotificationManager notificationManager;
	@Inject
	ConnectionRegistry connectionRegistry;
	@Inject
	ViewModelProvider.Factory viewModelFactory;
	@Inject
	FeatureFlags featureFlags;

	// Fields that are accessed from background threads must be volatile
	@Inject
	volatile ContactManager contactManager;
	@Inject
	volatile MessagingManager messagingManager;
	@Inject
	volatile ConversationManager conversationManager;
	@Inject
	volatile EventBus eventBus;
	@Inject
	volatile IntroductionManager introductionManager;
	@Inject
	volatile ForumSharingManager forumSharingManager;
	@Inject
	volatile BlogSharingManager blogSharingManager;
	@Inject
	volatile GroupInvitationManager groupInvitationManager;
	@Inject
 volatile AttachmentReader attachmentReader;

	private final Map<MessageId, String> textCache = new ConcurrentHashMap<>();
	private final Observer<String> contactNameObserver = name -> {
		requireNonNull(name);
		loadMessages();
	};

	private final ActivityResultLauncher<String[]> docLauncher =
			registerForActivityResult(new OpenMultipleImageDocumentsAdvanced(),
					this::onImagesChosen);
	private final ActivityResultLauncher<String> contentLauncher =
			registerForActivityResult(new GetMultipleImagesAdvanced(),
					this::onImagesChosen);

	private final ActivityResultLauncher<String[]> fileDocLauncher =
			registerForActivityResult(new OpenMultipleFilesAdvanced(),
					this::onFilesChosen);
	private final ActivityResultLauncher<String> fileContentLauncher =
			registerForActivityResult(new GetMultipleFilesAdvanced(),
					this::onFilesChosen);

	private AttachmentRetriever attachmentRetriever;
	private ConversationViewModel viewModel;
	private ConversationVisitor visitor;
	private ConversationAdapter adapter;
	private Toolbar toolbar;
	private CircleImageView toolbarAvatar;
	private ImageView toolbarStatus;
	private TextView toolbarTitle;
	private BaqqaRecyclerView list;
	private LinearLayoutManager layoutManager;
	private TextInputView textInputView;
	private TextSendController sendController;
	private SelectionTracker<String> tracker;
	@Nullable
	private Parcelable layoutManagerState;
	@Nullable
	private ActionMode actionMode;

	// voice note recording
	private final ActivityResultLauncher<String> recordPermissionLauncher =
			registerForActivityResult(new RequestPermission(), granted -> {
				if (granted) startVoiceRecording();
				else Toast.makeText(this, R.string.voice_recording_error,
						LENGTH_SHORT).show();
			});
	private final Handler recordHandler = new Handler(Looper.getMainLooper());

	// periodically refreshes the playing voice chip's progress/duration
	private final Handler playbackHandler = new Handler(Looper.getMainLooper());
	private final Runnable playbackTick = new Runnable() {
		@Override
		public void run() {
			MessageId id = currentAudioId;
			if (id == null || !audioPlaying || seekingAudio) return;
			// update the visible voice chip directly from the authoritative
			// playback state; a notifyItemChanged() would rebind (and
			// re-inflate) the whole ViewHolder on every tick, which does
			// not reliably refresh the visible chip
			Pair<Integer, ConversationMessageItem> pair =
					adapter.getMessageItem(id);
			if (pair != null) {
				RecyclerView.ViewHolder vh =
						list.getRecyclerView()
								.findViewHolderForAdapterPosition(
										pair.getFirst());
				if (vh instanceof ConversationMessageViewHolder) {
					((ConversationMessageViewHolder) vh)
							.updateVoiceProgress();
				}
			}
			playbackHandler.postDelayed(this, 500);
		}
	};

	// true while the user is dragging the voice timeline
	private boolean seekingAudio = false;
	// seek position in [0, 1000] while seekingAudio is true
	private int audioSeekProgress = 0;
	private final Runnable recordTick = this::updateRecordTimer;
	private VoiceNoteRecorder voiceRecorder;
	private View voiceRecordingBar;
	private TextView recordingTimer;
	private View textInputContainer;
	private ImageButton voiceButton;

	// voice note playback
	private final AudioPlayer audioPlayer = new AudioPlayer();
	private final Map<MessageId, Integer> audioDurations = new HashMap<>();
	@Nullable
	private MessageId currentAudioId;
	private boolean audioPlaying = false;
	@Nullable
	private File currentAudioFile;

	private volatile ContactId contactId;

	@Override
	public void injectActivity(ActivityComponent component) {
		component.inject(this);
		viewModel = new ViewModelProvider(this, viewModelFactory)
				.get(ConversationViewModel.class);
	}

	@Override
	public void onCreate(@Nullable Bundle state) {
		// Spurious lint warning - using END causes a crash
		@SuppressLint("RtlHardcoded")
		Transition slide = new Slide(RIGHT);
		slide.setDuration(TRANSITION_DURATION_MS);
		setSceneTransitionAnimation(slide, null, slide);
		super.onCreate(state);

		Intent i = getIntent();
		int id = i.getIntExtra(CONTACT_ID, -1);
		if (id == -1) throw new IllegalStateException();
		contactId = new ContactId(id);

		viewModel.setContactId(contactId);
		attachmentRetriever = viewModel.getAttachmentRetriever();

		setContentView(R.layout.activity_conversation);

		// Custom Toolbar
		toolbar = requireNonNull(setUpCustomToolbar(true));
		toolbarAvatar = toolbar.findViewById(R.id.contactAvatar);
		toolbarStatus = toolbar.findViewById(R.id.contactStatus);
		toolbarTitle = toolbar.findViewById(R.id.contactName);

		viewModel.getContactItem().observe(this, contactItem -> {
			requireNonNull(contactItem);
			setAvatar(toolbarAvatar, contactItem);
		});
		viewModel.getContactDisplayName().observe(this, contactName -> {
			requireNonNull(contactName);
			toolbarTitle.setText(contactName);
		});
		viewModel.isContactDeleted().observe(this, deleted -> {
			requireNonNull(deleted);
			if (deleted) finish();
		});
		viewModel.getAddedPrivateMessage().observeEvent(this,
				this::onAddedPrivateMessage);

		visitor = new ConversationVisitor(this, this, this,
				viewModel.getContactDisplayName());
		adapter = new ConversationAdapter(this, this);
		list = findViewById(R.id.conversationView);
		layoutManager = new LinearLayoutManager(this);
		list.setLayoutManager(layoutManager);
		list.setAdapter(adapter);
		list.setEmptyText(getString(R.string.no_private_messages));
		ConversationScrollListener scrollListener =
				new ConversationScrollListener(adapter, viewModel);
		list.getRecyclerView().addOnScrollListener(scrollListener);
		addSelectionTracker();

		textInputView = findViewById(R.id.text_input_container);
		if (featureFlags.shouldEnableImageAttachments()) {
			ImagePreview imagePreview = findViewById(R.id.imagePreview);
			sendController = new TextAttachmentController(textInputView,
					imagePreview, this, viewModel);
			observeOnce(viewModel.getPrivateMessageFormat(), this, format -> {
				if (format != TEXT_ONLY) {
					// TODO: remove cast when removing feature flag
					((TextAttachmentController) sendController)
							.setImagesSupported();
					((TextAttachmentController) sendController)
							.setFilesSupported();
				}
			});
		} else {
			sendController = new TextSendController(textInputView, this, false);
		}
		textInputView.setSendController(sendController);
		textInputView.setMaxTextLength(MAX_PRIVATE_MESSAGE_TEXT_LENGTH);
		textInputView.setReady(false);
		textInputView.setOnKeyboardShownListener(this::scrollToBottom);

		// voice note UI
		voiceButton = textInputView.findViewById(R.id.voiceButton);
		// show the voice button only here: 1:1 conversations support
		// voice notes via the attachment pipeline; TextInputView hides
		// it by default for text-only conversations (groups, forums)
		voiceButton.setVisibility(View.VISIBLE);
		voiceButton.setOnClickListener(v -> onVoiceButtonClicked());
		voiceRecordingBar = findViewById(R.id.voiceRecordingBar);
		recordingTimer = voiceRecordingBar.findViewById(R.id.recordingTimer);
		textInputContainer = findViewById(R.id.text_input_container);
		voiceRecorder = new VoiceNoteRecorder(getCacheDir());
		voiceRecordingBar.findViewById(R.id.cancelRecordingButton)
				.setOnClickListener(v -> cancelVoiceRecording());
		voiceRecordingBar.findViewById(R.id.finishRecordingButton)
				.setOnClickListener(v -> finishVoiceRecording());

		viewModel.getAutoDeleteTimer().observe(this, timer ->
				sendController.setAutoDeleteTimer(timer));
	}

	private void scrollToBottom() {
		int items = adapter.getItemCount();
		if (items > 0) list.scrollToPosition(items - 1);
	}

	@Override
	protected void onActivityResult(int request, int result,
			@Nullable Intent data) {
		super.onActivityResult(request, result, data);

		if (request == REQUEST_INTRODUCTION && result == RESULT_OK) {
			new BaqqaSnackbarBuilder()
					.make(list, R.string.introduction_sent,
							Snackbar.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		eventBus.addListener(this);
		notificationManager.blockContactNotification(contactId);
		notificationManager.clearContactNotification(contactId);
		displayContactOnlineStatus();
		viewModel.getContactDisplayName().observe(this, contactNameObserver);
		list.startPeriodicUpdate();
	}

	@Override
	public void onStop() {
		super.onStop();
		// release audio resources so nothing plays or records in the background
		recordHandler.removeCallbacks(recordTick);
		playbackHandler.removeCallbacks(playbackTick);
		if (voiceRecorder != null) voiceRecorder.cancel();
		stopAudioPlayback();
		textInputContainer = null;
		voiceButton = null;
		eventBus.removeListener(this);
		notificationManager.unblockContactNotification(contactId);
		viewModel.getContactDisplayName().removeObserver(contactNameObserver);
		list.stopPeriodicUpdate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (layoutManager != null) {
			layoutManagerState = layoutManager.onSaveInstanceState();
			outState.putParcelable("layoutManager", layoutManagerState);
		}
		if (tracker != null) tracker.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		layoutManagerState = savedInstanceState.getParcelable("layoutManager");
		if (tracker != null) tracker.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.conversation_actions, menu);

		// enable introduction action if available
		observeOnce(viewModel.showIntroductionAction(), this, enable -> {
			if (enable != null && enable) {
				menu.findItem(R.id.action_introduction).setEnabled(true);
				// show introduction onboarding, if needed
				viewModel.showIntroductionOnboarding().observeEvent(this,
						this::showIntroductionOnboarding);
			}
		});
		// enable alias and bluetooth action once available
		observeOnce(viewModel.getContactItem(), this, contact -> {
			menu.findItem(R.id.action_set_alias).setEnabled(true);
			menu.findItem(R.id.action_connect_via_bluetooth).setEnabled(true);
		});
		// Show auto-delete menu item if feature is enabled
		if (featureFlags.shouldEnableDisappearingMessages()) {
			MenuItem item = menu.findItem(R.id.action_conversation_settings);
			item.setVisible(true);
			// Enable menu item only if contact supports auto-delete
			viewModel.getPrivateMessageFormat().observe(this, format ->
					item.setEnabled(format == TEXT_IMAGES_AUTO_DELETE));
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// contactId gets set before in onCreate()
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			onBackPressed();
			return true;
		} else if (itemId == R.id.action_introduction) {
			Intent intent = new Intent(this, IntroductionActivity.class);
			intent.putExtra(CONTACT_ID, contactId.getInt());
			startActivityForResult(intent, REQUEST_INTRODUCTION);
			return true;
		} else if (itemId == R.id.action_set_alias) {
			AliasDialogFragment.newInstance().show(
					getSupportFragmentManager(), AliasDialogFragment.TAG);
			return true;
		} else if (itemId == R.id.action_conversation_settings) {
			onAutoDeleteTimerNoticeClicked();
			return true;
		} else if (itemId == R.id.action_connect_via_bluetooth) {
			Intent intent = new Intent(this, ConnectViaBluetoothActivity.class);
			intent.putExtra(CONTACT_ID, contactId.getInt());
			startActivity(intent);
			return true;
		} else if (itemId == R.id.action_transfer_data) {
			Intent intent = new Intent(this, RemovableDriveActivity.class);
			intent.putExtra(CONTACT_ID, contactId.getInt());
			startActivity(intent);
			return true;
		} else if (itemId == R.id.action_delete_all_messages) {
			askToDeleteAllMessages();
			return true;
		} else if (itemId == R.id.action_social_remove_person) {
			askToRemoveContact();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.conversation_message_actions, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false; // no update needed
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (item.getItemId() == R.id.action_delete) {
			deleteSelectedMessages();
			return true;
		}
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		tracker.clearSelection();
		actionMode = null;
	}

	@Override
	public void onLinkClick(String url) {
		LinkDialogFragment f = LinkDialogFragment.newInstance(url);
		f.show(getSupportFragmentManager(), f.getUniqueTag());
	}

	private void addSelectionTracker() {
		RecyclerView recyclerView = list.getRecyclerView();
		if (recyclerView.getAdapter() != adapter)
			throw new IllegalStateException();

		tracker = new SelectionTracker.Builder<>(
				"conversationSelection",
				recyclerView,
				new ConversationItemKeyProvider(adapter),
				new ConversationItemDetailsLookup(recyclerView),
				StorageStrategy.createStringStorage()
		).withSelectionPredicate(
				SelectionPredicates.createSelectAnything()
		).build();

		SelectionObserver<String> observer = new SelectionObserver<String>() {
			@Override
			public void onItemStateChanged(String key, boolean selected) {
				if (selected && actionMode == null) {
					actionMode = startActionMode(ConversationActivity.this);
					updateActionModeTitle();
				} else if (actionMode != null) {
					if (selected || tracker.hasSelection()) {
						updateActionModeTitle();
					} else {
						actionMode.finish();
					}
				}
			}
		};
		tracker.addObserver(observer);
		adapter.setSelectionTracker(tracker);
	}

	private void updateActionModeTitle() {
		if (actionMode == null) throw new IllegalStateException();
		String title = String.valueOf(tracker.getSelection().size());
		actionMode.setTitle(title);
	}

	private Collection<MessageId> getSelection() {
		Selection<String> selection = tracker.getSelection();
		List<MessageId> messages = new ArrayList<>(selection.size());
		for (String str : selection) {
			try {
				MessageId id = new MessageId(fromHexString(str));
				messages.add(id);
			} catch (FormatException e) {
				LOG.warning("Invalid message id");
			}
		}
		return messages;
	}

	@UiThread
	private void displayContactOnlineStatus() {
		if (connectionRegistry.isConnected(contactId)) {
			toolbarStatus.setImageResource(R.drawable.contact_online);
			toolbarStatus.setContentDescription(getString(R.string.online));
		} else {
			toolbarStatus.setImageResource(R.drawable.contact_offline);
			toolbarStatus.setContentDescription(getString(R.string.offline));
		}
	}

	private void loadMessages() {
		int revision = adapter.getRevision();
		runOnDbThread(() -> {
			try {
				long start = now();
				Collection<ConversationMessageHeader> headers =
						conversationManager.getMessageHeaders(contactId);
				logDuration(LOG, "Loading messages", start);
				// Sort headers by timestamp in *descending* order
				List<ConversationMessageHeader> sorted =
						new ArrayList<>(headers);
				sort(sorted, (a, b) ->
						Long.compare(b.getTimestamp(), a.getTimestamp()));
				if (!sorted.isEmpty()) {
					// If the latest header is a private message, eagerly load
					// its size so we can set the scroll position correctly
					ConversationMessageHeader latest = sorted.get(0);
					if (latest instanceof PrivateMessageHeader) {
						eagerlyLoadMessageSize((PrivateMessageHeader) latest);
					}
				}
				displayMessages(revision, sorted);
			} catch (NoSuchContactException e) {
				finishOnUiThread();
			} catch (DbException e) {
				logException(LOG, WARNING, e);
			}
		});
	}

	@DatabaseExecutor
	private void eagerlyLoadMessageSize(PrivateMessageHeader h) {
		try {
			MessageId id = h.getId();
			// If the message has text, load it
			if (h.hasText()) {
				String text = textCache.get(id);
				if (text == null) {
					LOG.info("Eagerly loading text for latest message");
					text = messagingManager.getMessageText(id);
					textCache.put(id, requireNonNull(text));
				}
			}
			// If the message has a single image, load its size - for multiple
			// images we use a grid so the size is fixed
			List<AttachmentHeader> headers = h.getAttachmentHeaders();
			if (headers.size() == 1) {
				LOG.info("Eagerly loading image size for latest message");
				AttachmentHeader header = headers.get(0);
				// get the item to retrieve its size
				attachmentRetriever
						.cacheAttachmentItemWithSize(h.getId(), header);
			}
		} catch (DbException e) {
			logException(LOG, WARNING, e);
		}
	}

	private void displayMessages(int revision,
			Collection<ConversationMessageHeader> headers) {
		runOnUiThreadUnlessDestroyed(() -> {
			if (revision == adapter.getRevision()) {
				adapter.incrementRevision();
				textInputView.setReady(true);
				// start observing onboarding after enabling
				if (featureFlags.shouldEnableImageAttachments()) {
					viewModel.showImageOnboarding().observeEvent(this,
							this::showImageOnboarding);
				}
				List<ConversationItem> items = createItems(headers);
				adapter.replaceAll(items);
				list.showData();
				if (layoutManagerState == null) {
					scrollToBottom();
				} else {
					// Restore the previous scroll position
					layoutManager.onRestoreInstanceState(layoutManagerState);
				}
			} else {
				LOG.info("Concurrent update, reloading");
				loadMessages();
			}
		});
	}

	/**
	 * Creates ConversationItems from headers loaded from the database.
	 * <p>
	 * Attention: Call this only after contactName has been initialized.
	 */
	private List<ConversationItem> createItems(
			Collection<ConversationMessageHeader> headers) {
		List<ConversationItem> items = new ArrayList<>(headers.size());
		for (ConversationMessageHeader h : headers)
			items.add(h.accept(visitor));
		return items;
	}

	private void loadMessageText(MessageId m) {
		runOnDbThread(() -> {
			try {
				long start = now();
				String text = messagingManager.getMessageText(m);
				logDuration(LOG, "Loading text", start);
				displayMessageText(m, requireNonNull(text));
			} catch (DbException e) {
				logException(LOG, WARNING, e);
			}
		});
	}

	private void displayMessageText(MessageId m, String text) {
		runOnUiThreadUnlessDestroyed(() -> {
			textCache.put(m, text);
			Pair<Integer, ConversationMessageItem> pair =
					adapter.getMessageItem(m);
			if (pair != null) {
				boolean scroll = shouldScrollWhenUpdatingMessage();
				pair.getSecond().setText(text);
				adapter.notifyItemChanged(pair.getFirst());
				if (scroll) scrollToBottom();
			}
		});
	}

	// When a message's text or attachments are loaded, scroll to the bottom
	// if the conversation is visible and we were previously at the bottom
	private boolean shouldScrollWhenUpdatingMessage() {
		return getLifecycle().getCurrentState().isAtLeast(STARTED)
				&& adapter.isScrolledToBottom(layoutManager);
	}

	@UiThread
	private void updateMessageAttachment(MessageId m, AttachmentItem item) {
		Pair<Integer, ConversationMessageItem> pair = adapter.getMessageItem(m);
		if (pair != null && pair.getSecond().updateAttachments(item)) {
			boolean scroll = shouldScrollWhenUpdatingMessage();
			adapter.notifyItemChanged(pair.getFirst());
			if (scroll) scrollToBottom();
		}
	}

	@Override
	public void eventOccurred(Event e) {
		if (e instanceof ContactRemovedEvent) {
			ContactRemovedEvent c = (ContactRemovedEvent) e;
			if (c.getContactId().equals(contactId)) {
				LOG.info("Contact removed");
				supportFinishAfterTransition();
			}
		} else if (e instanceof ConversationMessageReceivedEvent) {
			ConversationMessageReceivedEvent<?> p =
					(ConversationMessageReceivedEvent<?>) e;
			if (p.getContactId().equals(contactId)) {
				LOG.info("Message received, adding");
				onNewConversationMessage(p.getMessageHeader());
			}
		} else if (e instanceof MessagesSentEvent) {
			MessagesSentEvent m = (MessagesSentEvent) e;
			if (m.getContactId().equals(contactId)) {
				LOG.info("Messages sent");
				markMessages(m.getMessageIds(), true, false);
			}
		} else if (e instanceof MessagesAckedEvent) {
			MessagesAckedEvent m = (MessagesAckedEvent) e;
			if (m.getContactId().equals(contactId)) {
				LOG.info("Messages acked");
				markMessages(m.getMessageIds(), true, true);
			}
		} else if (e instanceof ConversationMessagesDeletedEvent) {
			ConversationMessagesDeletedEvent m =
					(ConversationMessagesDeletedEvent) e;
			if (m.getContactId().equals(contactId)) {
				LOG.info("Messages auto-deleted");
				onConversationMessagesDeleted(m.getMessageIds());
			}
		} else if (e instanceof ContactConnectedEvent) {
			ContactConnectedEvent c = (ContactConnectedEvent) e;
			if (c.getContactId().equals(contactId)) {
				LOG.info("Contact connected");
				displayContactOnlineStatus();
			}
		} else if (e instanceof ContactDisconnectedEvent) {
			ContactDisconnectedEvent c = (ContactDisconnectedEvent) e;
			if (c.getContactId().equals(contactId)) {
				LOG.info("Contact disconnected");
				displayContactOnlineStatus();
			}
		} else if (e instanceof ClientVersionUpdatedEvent) {
			ClientVersionUpdatedEvent c = (ClientVersionUpdatedEvent) e;
			if (c.getContactId().equals(contactId)) {
				ClientId clientId = c.getClientVersion().getClientId();
				if (clientId.equals(MessagingManager.CLIENT_ID)) {
					LOG.info("Contact's messaging client was updated");
					viewModel.recheckFeaturesAndOnboarding(contactId);
				}
			}
		}
	}

	@UiThread
	private void addConversationItem(ConversationItem item) {
		adapter.incrementRevision();
		adapter.add(item);
		// When adding a new message, scroll to the bottom if the conversation
		// is visible, even if we're not currently at the bottom
		if (getLifecycle().getCurrentState().isAtLeast(STARTED))
			scrollToBottom();
	}

	@UiThread
	private void onNewConversationMessage(ConversationMessageHeader h) {
		if (h instanceof ConversationRequest ||
				h instanceof ConversationResponse) {
			// contact name might not have been loaded
			observeOnce(viewModel.getContactDisplayName(), this,
					name -> addConversationItem(h.accept(visitor)));
		} else {
			// visitor also loads message text and attachments (if existing)
			addConversationItem(h.accept(visitor));
		}
	}

	@UiThread
	private void onConversationMessagesDeleted(
			Collection<MessageId> messageIds) {
		adapter.incrementRevision();
		adapter.removeItems(messageIds);
	}

	@UiThread
	private void markMessages(Collection<MessageId> messageIds, boolean sent,
			boolean seen) {
		adapter.incrementRevision();
		Set<MessageId> messages = new HashSet<>(messageIds);
		SparseArray<ConversationItem> list = adapter.getOutgoingMessages();
		for (int i = 0; i < list.size(); i++) {
			ConversationItem item = list.valueAt(i);
			if (messages.contains(item.getId())) {
				item.setSent(sent);
				item.setSeen(seen);
				adapter.notifyItemChanged(list.keyAt(i));
			}
		}
	}

	@Override
	public void onAttachImageClicked() {
		launchActivityToOpenFile(this, docLauncher, contentLauncher, "image/*");
	}

	@Override
	public void onAttachFileClicked() {
		launchActivityToOpenFile(this, fileDocLauncher, fileContentLauncher,
				"*/*");
	}

	private void onImagesChosen(@Nullable List<Uri> uris) {
		// TODO: remove cast when removing feature flag
		((TextAttachmentController) sendController).onImageReceived(uris);
	}

	private void onFilesChosen(@Nullable List<Uri> uris) {
		((TextAttachmentController) sendController).onFilesReceived(uris);
	}

	@Override
	public void onTooManyAttachments() {
		String format = getResources().getString(
				R.string.messaging_too_many_attachments_toast);
		String warning = String.format(format, MAX_ATTACHMENTS_PER_MESSAGE);
		Toast.makeText(this, warning, LENGTH_SHORT).show();
	}

	@Override
	public LiveData<SendState> onSendClick(@Nullable String text,
			List<AttachmentHeader> attachmentHeaders,
			long expectedAutoDeleteTimer) {
		if (isNullOrEmpty(text) && attachmentHeaders.isEmpty())
			throw new AssertionError();
		return viewModel
				.sendMessage(text, attachmentHeaders, expectedAutoDeleteTimer);
	}

	private void onAddedPrivateMessage(@Nullable PrivateMessageHeader h) {
		if (h == null) return;
		addConversationItem(h.accept(visitor));
	}

	private void askToDeleteAllMessages() {
		MaterialAlertDialogBuilder builder =
				new MaterialAlertDialogBuilder(this, R.style.BaqqaDialogTheme);
		builder.setTitle(getString(R.string.dialog_title_delete_all_messages));
		builder.setMessage(
				getString(R.string.dialog_message_delete_all_messages));
		builder.setNegativeButton(R.string.delete,
				(dialog, which) -> deleteAllMessages());
		builder.setPositiveButton(R.string.cancel, null);
		builder.show();
	}

	private void deleteAllMessages() {
		list.showProgressBar();
		runOnDbThread(() -> {
			try {
				DeletionResult result =
						conversationManager.deleteAllMessages(contactId);
				reloadConversationAfterDeletingMessages(result);
			} catch (DbException e) {
				logException(LOG, WARNING, e);
				runOnUiThreadUnlessDestroyed(() -> list.showData());
			}
		});
	}

	private void deleteSelectedMessages() {
		list.showProgressBar();
		Collection<MessageId> selected = getSelection();
		// close action mode only after getting the selection
		if (actionMode != null) actionMode.finish();
		runOnDbThread(() -> {
			try {
				DeletionResult result =
						conversationManager.deleteMessages(contactId, selected);
				reloadConversationAfterDeletingMessages(result);
			} catch (DbException e) {
				logException(LOG, WARNING, e);
				runOnUiThreadUnlessDestroyed(() -> list.showData());
			}
		});
	}

	private void reloadConversationAfterDeletingMessages(
			DeletionResult result) {
		runOnUiThreadUnlessDestroyed(() -> {
			adapter.clear();
			list.showProgressBar();  // otherwise clearing shows empty state
			loadMessages();
			if (!result.allDeleted()) showNotAllDeletedDialog(result);
		});
	}

	private void showNotAllDeletedDialog(DeletionResult result) {
		List<String> fails = new ArrayList<>();
		// get failures the user cannot immediately resolve
		if (result.hasIntroductionSessionInProgress() &&
				result.hasInvitationSessionInProgress()) {
			fails.add(getString(
					R.string.dialog_message_not_deleted_ongoing_both));
		} else if (result.hasIntroductionSessionInProgress()) {
			fails.add(getString(
					R.string.dialog_message_not_deleted_ongoing_introductions));
		} else if (result.hasInvitationSessionInProgress()) {
			fails.add(getString(
					R.string.dialog_message_not_deleted_ongoing_invitations));
		}
		// add problems the user can resolve
		if (result.hasNotAllIntroductionSelected() &&
				result.hasNotAllInvitationSelected()) {
			fails.add(getString(
					R.string.dialog_message_not_deleted_not_all_selected_both));
		} else if (result.hasNotAllIntroductionSelected()) {
			fails.add(getString(
					R.string.dialog_message_not_deleted_not_all_selected_introductions));
		} else if (result.hasNotAllInvitationSelected()) {
			fails.add(getString(
					R.string.dialog_message_not_deleted_not_all_selected_invitations));
		}
		String msg = join(fails, "\n\n");
		// show dialog
		MaterialAlertDialogBuilder builder =
				new MaterialAlertDialogBuilder(this, R.style.BaqqaDialogTheme);
		builder.setTitle(
				getString(R.string.dialog_title_not_all_messages_deleted));
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.ok, null);
		builder.show();
	}

	private void askToRemoveContact() {
		DialogInterface.OnClickListener okListener =
				(dialog, which) -> removeContact();
		MaterialAlertDialogBuilder builder =
				new MaterialAlertDialogBuilder(ConversationActivity.this,
						R.style.BaqqaDialogTheme);
		builder.setTitle(getString(R.string.dialog_title_delete_contact));
		builder.setMessage(
				getString(R.string.dialog_message_delete_contact));
		builder.setNegativeButton(R.string.delete, okListener);
		builder.setPositiveButton(R.string.cancel, null);
		builder.show();
	}

	private void removeContact() {
		list.showProgressBar();
		runOnDbThread(() -> {
			try {
				contactManager.removeContact(contactId);
			} catch (DbException e) {
				logException(LOG, WARNING, e);
			} finally {
				finishAfterContactRemoved();
			}
		});
	}

	private void finishAfterContactRemoved() {
		runOnUiThreadUnlessDestroyed(() -> {
			String deleted = getString(R.string.contact_deleted_toast);
			Toast.makeText(ConversationActivity.this, deleted, LENGTH_SHORT)
					.show();
			supportFinishAfterTransition();
		});
	}

	private void showImageOnboarding(Boolean show) {
		if (!show) return;
		// show onboarding only after the enter transition has ended
		// otherwise the tap target animation won't play
		textInputView.postDelayed(this::showImageOnboarding,
				TRANSITION_DURATION_MS + ONBOARDING_DELAY_MS);
	}

	private void showImageOnboarding() {
		// TODO: remove cast when removing feature flag
		((TextAttachmentController) sendController)
				.showImageOnboarding(this);
	}

	private void showIntroductionOnboarding(@Nullable Boolean show) {
		if (show == null || !show) return;
		// show onboarding only after the enter transition has ended
		// otherwise the tap target animation won't play
		textInputView.postDelayed(this::showIntroductionOnboarding,
				TRANSITION_DURATION_MS + ONBOARDING_DELAY_MS);
	}

	private void showIntroductionOnboarding() {
		// find view of overflow icon
		View target = null;
		for (int i = 0; i < toolbar.getChildCount(); i++) {
			if (toolbar.getChildAt(i) instanceof ActionMenuView) {
				ActionMenuView menu =
						(ActionMenuView) toolbar.getChildAt(i);
				// The overflow icon should be the last child of the menu
				target = menu.getChildAt(menu.getChildCount() - 1);
				// If the menu hasn't been populated yet, use the menu itself
				// as the target
				if (target == null) target = menu;
				break;
			}
		}
		if (target == null) {
			LOG.warning("No Overflow Icon found!");
			return;
		}

		int color =
				ContextCompat.getColor(this, R.color.baqqa_primary);
		Drawable drawable = VectorDrawableCompat
				.create(getResources(), R.drawable.ic_more_vert_accent, null);
		new MaterialTapTargetPrompt.Builder(ConversationActivity.this,
				R.style.OnboardingDialogTheme).setTarget(target)
				.setPrimaryText(R.string.introduction_onboarding_title)
				.setSecondaryText(R.string.introduction_onboarding_text)
				.setIconDrawable(drawable)
				.setBackgroundColour(color)
				.show();
	}

	@UiThread
	@Override
	public void respondToRequest(ConversationRequestItem item,
			boolean accept) {
		item.setAnswered();
		int position = adapter.findItemPosition(item);
		if (position != INVALID_POSITION) {
			adapter.notifyItemChanged(position, item);
		}
		runOnDbThread(() -> {
			try {
				switch (item.getRequestType()) {
					case INTRODUCTION:
						respondToIntroductionRequest(item.getSessionId(),
								accept);
						break;
					case FORUM:
						respondToForumRequest(item.getSessionId(), accept);
						break;
					case BLOG:
						respondToBlogRequest(item.getSessionId(), accept);
						break;
					case GROUP:
						respondToGroupRequest(item.getSessionId(), accept);
						break;
					default:
						throw new IllegalArgumentException(
								"Unknown Request Type");
				}
				loadMessages();
			} catch (ProtocolStateException e) {
				// Action is no longer valid - reloading should solve the issue
				logException(LOG, INFO, e);
			} catch (DbException e) {
				// TODO show an error message
				logException(LOG, WARNING, e);
			}
		});
	}

	@UiThread
	@Override
	public void openRequestedShareable(ConversationRequestItem item) {
		if (item.getRequestedGroupId() == null)
			throw new IllegalArgumentException();
		Intent i;
		switch (item.getRequestType()) {
			case FORUM:
				i = new Intent(this, ForumActivity.class);
				break;
			case BLOG:
				i = new Intent(this, BlogActivity.class);
				break;
			case GROUP:
				i = new Intent(this, GroupActivity.class);
				break;
			default:
				throw new IllegalArgumentException("Unknown Request Type");
		}
		i.putExtra(GROUP_ID, item.getRequestedGroupId().getBytes());
		startActivity(i);
	}

	@Override
	public void onAttachmentClicked(View view,
			ConversationMessageItem messageItem, AttachmentItem item) {
		if (item.isAudio()) {
			toggleAudioPlayback(item);
		} else if (item.isImage()) {
			// image attachment: open in full-screen image viewer
			String name;
			if (messageItem.isIncoming()) {
				// must be available when items are being displayed
				name = viewModel.getContactDisplayName().getValue();
			} else {
				name = getString(R.string.you);
			}
			ArrayList<AttachmentItem> attachments =
					new ArrayList<>(messageItem.getAttachments());
			Intent i = new Intent(this, ImageActivity.class);
			i.putParcelableArrayListExtra(ATTACHMENTS, attachments);
			i.putExtra(ATTACHMENT_POSITION, attachments.indexOf(item));
			i.putExtra(NAME, name);
			i.putExtra(DATE, messageItem.getTime());
			i.putExtra(ITEM_ID, messageItem.getId().getBytes());
			// restoring list position should not trigger android bug #224270
			String transitionName =
					item.getTransitionName(messageItem.getId());
			ActivityOptionsCompat options =
					makeSceneTransitionAnimation(this, view,
							transitionName);
			ActivityCompat.startActivity(this, i, options.toBundle());
		} else {
			// generic file attachment: open with external app via ACTION_VIEW
			openFileAttachment(item);
		}
	}

	private void openFileAttachment(AttachmentItem item) {
		File attachmentsDir = new File(getCacheDir(), "attachments");
		if (!attachmentsDir.exists()) attachmentsDir.mkdirs();
		String fileName = item.getFileName();
		if (fileName == null) fileName = "attachment." + item.getExtension();
		if (fileName.equals("")) fileName = "attachment";
		File outFile = new File(attachmentsDir, fileName);
		try {
			Attachment a = attachmentReader.getAttachment(item.getHeader());
			try (InputStream in = a.getStream();
			     OutputStream out = new FileOutputStream(outFile)) {
				byte[] buf = new byte[8192];
				int len;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
			}
			String authority = getPackageName() + ".fileprovider";
			Uri uri = FileProvider.getUriForFile(this, authority,
					outFile);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(uri, item.getMimeType());
			intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
			UiUtils.tryToStartActivity(this, intent);
		} catch (Exception e) {
			logException(LOG, WARNING, e);
			Toast.makeText(this, R.string.error_start_activity,
					Toast.LENGTH_LONG).show();
		}
	}

	// ----- voice note playback -----

	private void toggleAudioPlayback(AttachmentItem item) {
		if (item.getMessageId().equals(currentAudioId)
				&& currentAudioFile != null) {
			// toggle the currently prepared audio
			if (audioPlaying) audioPlayer.pause();
			else audioPlayer.resume();
		} else {
			startAudioPlayback(item);
		}
	}

	private void startAudioPlayback(AttachmentItem item) {
		stopAudioPlayback();
		currentAudioId = item.getMessageId();
		// export the attachment to a private cache file for playback
		File attachmentsDir = new File(getCacheDir(), "attachments");
		if (!attachmentsDir.exists()) attachmentsDir.mkdirs();
		File outFile = new File(attachmentsDir, "voice_" +
				item.getMessageId().hashCode() + ".m4a");
		try {
			Attachment a = attachmentReader.getAttachment(item.getHeader());
			try (InputStream in = a.getStream();
			     OutputStream out = new FileOutputStream(outFile)) {
				byte[] buf = new byte[8192];
				int len;
				while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
			}
		} catch (Exception e) {
			logException(LOG, WARNING, e);
			currentAudioId = null;
			Toast.makeText(this, R.string.voice_message_error,
					LENGTH_SHORT).show();
			return;
		}
		currentAudioFile = outFile;
		audioPlayer.play(this, outFile, new AudioPlayer.Listener() {
			@Override
			public void onPlaying() {
				audioPlaying = true;
				cacheAudio();
				notifyAudioChanged();
				playbackHandler.removeCallbacks(playbackTick);
				playbackHandler.postDelayed(playbackTick, 500);
			}

			@Override
			public void onPaused() {
				audioPlaying = false;
				playbackHandler.removeCallbacks(playbackTick);
				cacheAudio();
				notifyAudioChanged();
			}

			@Override
			public void onStopped() {
				audioPlaying = false;
				playbackHandler.removeCallbacks(playbackTick);
				notifyAudioChanged();
				finishAudioPlayback();
			}

			@Override
			public void onCompleted() {
				// reached the end of the audio; keep the player loaded at
				// the end position so the user can seek backwards/replay
				audioPlaying = false;
				playbackHandler.removeCallbacks(playbackTick);
				cacheAudio();
				notifyAudioChanged();
			}

			@Override
			public void onSeekComplete() {
				// a seek finished; resume normal real-position refreshes
				playbackHandler.removeCallbacks(playbackTick);
				if (audioPlaying && !seekingAudio) {
					playbackHandler.postDelayed(playbackTick, 500);
				}
				notifyAudioChanged();
			}

			@Override
			public void onError() {
				audioPlaying = false;
				playbackHandler.removeCallbacks(playbackTick);
				notifyAudioChanged();
				finishAudioPlayback();
				Toast.makeText(ConversationActivity.this,
						R.string.voice_message_error, LENGTH_SHORT).show();
			}
		});
	}

	private void cacheAudio() {
		MessageId id = currentAudioId;
		int duration = audioPlayer.getDurationMillis();
		if (id != null && duration > 0) audioDurations.put(id, duration);
	}

	private void finishAudioPlayback() {
		currentAudioId = null;
		audioPlaying = false;
		deleteAudioFile();
	}

	private void notifyAudioChanged() {
		adapter.notifyDataSetChanged();
	}

	private void stopAudioPlayback() {
		audioPlayer.release();
		currentAudioId = null;
		seekingAudio = false;
		audioPlaying = false;
		deleteAudioFile();
	}

	private void deleteAudioFile() {
		File file = currentAudioFile;
		currentAudioFile = null;
		if (file != null && !file.delete()) {
			LOG.warning("Could not delete temporary audio file");
		}
	}

	@Override
	@UiThread
	public boolean isAudioPlaying(AttachmentItem item) {
		return currentAudioId != null
				&& currentAudioId.equals(item.getMessageId())
				&& audioPlaying;
	}

	@UiThread
	boolean isAudioPaused(AttachmentItem item) {
		return currentAudioId != null
				&& currentAudioId.equals(item.getMessageId())
				&& !audioPlaying;
	}

	/**
	 * Returns the playback progress as a percentage in [0, 1000] for the given
	 * item if it is currently loaded, or 0 otherwise.
	 */
	@Override
	@UiThread
	public int getAudioProgress(AttachmentItem item) {
		if (!isAudioPlaying(item) && !isAudioPaused(item)) return 0;
		// while the user is dragging the timeline, show the seek position
		if (seekingAudio && currentAudioId != null
				&& currentAudioId.equals(item.getMessageId())) {
			return audioSeekProgress;
		}
		int duration = audioPlayer.getDurationMillis();
		if (duration <= 0) return 0;
		int position = audioPlayer.getCurrentPositionMillis();
		if (position < 0) return 0;
		return Math.min(1000, (int) (position * 1000L / duration));
	}

	@Override
	@UiThread
	public int getAudioDurationMillis(AttachmentItem item) {
		if (currentAudioId != null
				&& currentAudioId.equals(item.getMessageId())) {
			return audioPlayer.getDurationMillis();
		}
		return -1;
	}

	@Override
	@UiThread
	public void onAudioSeekStarted(AttachmentItem item) {
		// pause real-position refreshes so they cannot overwrite the drag
		seekingAudio = true;
		playbackHandler.removeCallbacks(playbackTick);
	}

	@Override
	@UiThread
	public void onAudioSeekPreview(AttachmentItem item, int progress) {
		if (seekingAudio && currentAudioId != null
				&& currentAudioId.equals(item.getMessageId())) {
			audioSeekProgress = progress;
		}
	}

	@Override
	@UiThread
	public void onAudioSeekCommitted(AttachmentItem item, int progress) {
		seekingAudio = false;
		if (currentAudioId == null
				|| !currentAudioId.equals(item.getMessageId())
				|| currentAudioFile == null) {
			return;
		}
		int duration = audioPlayer.getDurationMillis();
		if (duration > 0) {
			audioPlayer.seekTo((int) (progress * (long) duration / 1000L));
			// onSeekComplete() refreshes the UI with the real position
		} else {
			notifyAudioChanged();
		}
	}

	/**
	 * Returns a compact duration label for the given item, e.g. "0:12 / 1:23",
	 * or an empty string if the duration is not known yet.
	 */
	@Override
	@UiThread
	public String getAudioDurationText(AttachmentItem item) {
		if (isAudioPlaying(item) || isAudioPaused(item)) {
			int duration = audioPlayer.getDurationMillis();
			int position;
			if (seekingAudio && currentAudioId != null
					&& currentAudioId.equals(item.getMessageId())) {
				// show the position the user is dragging to
				position = duration > 0
						? (int) (audioSeekProgress * (long) duration / 1000L)
						: -1;
			} else {
				position = audioPlayer.getCurrentPositionMillis();
			}
			if (duration > 0 && position >= 0) {
				return VoiceNote.formatSeconds(position / 1000) + " / " +
						VoiceNote.formatSeconds(duration / 1000);
			}
		}
		Integer duration = audioDurations.get(item.getMessageId());
		if (duration != null && duration > 0) {
			return VoiceNote.formatSeconds(duration / 1000);
		}
		return "";
	}

	// ----- voice note recording -----

	private void onVoiceButtonClicked() {
		if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) ==
				PERMISSION_GRANTED) {
			startVoiceRecording();
		} else {
			recordPermissionLauncher.launch(RECORD_AUDIO);
		}
	}

	private void startVoiceRecording() {
		try {
			voiceRecorder.start();
		} catch (Exception e) {
			logException(LOG, WARNING, e);
			Toast.makeText(this, R.string.voice_recording_error, LENGTH_SHORT)
					.show();
			return;
		}
		textInputContainer.setVisibility(View.GONE);
		voiceRecordingBar.setVisibility(View.VISIBLE);
		updateRecordTimer();
	}

	private void updateRecordTimer() {
		long ms = voiceRecorder.getDurationMillis();
		if (ms >= 0) {
			recordingTimer.setText(getString(R.string.voice_recording_timer,
					VoiceNote.formatSeconds(ms / 1000)));
			recordHandler.postDelayed(recordTick, 200);
		}
	}

	private void cancelVoiceRecording() {
		recordHandler.removeCallbacks(recordTick);
		voiceRecorder.cancel();
		textInputContainer.setVisibility(View.VISIBLE);
		voiceRecordingBar.setVisibility(View.GONE);
	}

	private void finishVoiceRecording() {
		recordHandler.removeCallbacks(recordTick);
		File file = voiceRecorder.stop();
		textInputContainer.setVisibility(View.VISIBLE);
		voiceRecordingBar.setVisibility(View.GONE);
		if (file == null) {
			Toast.makeText(this, R.string.voice_short_recording,
					LENGTH_SHORT).show();
			return;
		}
		// stage the recording through the existing attachment pipeline
		Uri uri = AudioFileProvider.getUriForFile(this, file);
		((TextAttachmentController) sendController).onFilesReceived(
				singletonList(uri));
	}

	@Override
	public void onAutoDeleteTimerNoticeClicked() {
		ConversationSettingsDialog dialog =
				ConversationSettingsDialog.newInstance(contactId);
		dialog.show(getSupportFragmentManager(),
				ConversationSettingsDialog.TAG);
	}

	@DatabaseExecutor
	private void respondToIntroductionRequest(SessionId sessionId,
			boolean accept) throws DbException {
		introductionManager.respondToIntroduction(contactId, sessionId, accept);
	}

	@DatabaseExecutor
	private void respondToForumRequest(SessionId id, boolean accept)
			throws DbException {
		forumSharingManager.respondToInvitation(contactId, id, accept);
	}

	@DatabaseExecutor
	private void respondToBlogRequest(SessionId id, boolean accept)
			throws DbException {
		blogSharingManager.respondToInvitation(contactId, id, accept);
	}

	@DatabaseExecutor
	private void respondToGroupRequest(SessionId id, boolean accept)
			throws DbException {
		groupInvitationManager.respondToInvitation(contactId, id, accept);
	}

	@Nullable
	@Override
	public String getText(MessageId m) {
		String text = textCache.get(m);
		if (text == null) loadMessageText(m);
		return text;
	}

	/**
	 * Called by {@link PrivateMessageHeader#accept(ConversationMessageVisitor)}
	 */
	@Override
	public List<AttachmentItem> getAttachmentItems(PrivateMessageHeader h) {
		List<LiveData<AttachmentItem>> liveDataList =
				attachmentRetriever.getAttachmentItems(h);
		List<AttachmentItem> items = new ArrayList<>(liveDataList.size());
		for (LiveData<AttachmentItem> liveData : liveDataList) {
			// first remove all our observers to avoid having more than one
			// in case we reload the conversation, e.g. after deleting messages
			liveData.removeObservers(this);
			// add a new observer
			liveData.observe(this, new AttachmentObserver(h.getId(), liveData));
			items.add(requireNonNull(liveData.getValue()));
		}
		return items;
	}

	private class AttachmentObserver implements Observer<AttachmentItem> {
		private final MessageId conversationMessageId;
		private final LiveData<AttachmentItem> liveData;

		private AttachmentObserver(MessageId conversationMessageId,
				LiveData<AttachmentItem> liveData) {
			this.conversationMessageId = conversationMessageId;
			this.liveData = liveData;
		}

		@Override
		public void onChanged(AttachmentItem attachmentItem) {
			updateMessageAttachment(conversationMessageId, attachmentItem);
			if (attachmentItem.getState().isFinal())
				liveData.removeObserver(this);
		}
	}

}




