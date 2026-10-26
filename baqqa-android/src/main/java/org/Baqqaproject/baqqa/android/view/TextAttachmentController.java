package org.Baqqaproject.Baqqa.android.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentItemResult;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentManager;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentResult;
import org.Baqqaproject.Baqqa.android.view.ImagePreview.ImagePreviewListener;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.customview.view.AbsSavedState;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_LONG;
import static androidx.core.content.ContextCompat.getColor;
import static androidx.customview.view.AbsSavedState.EMPTY_STATE;
import static androidx.lifecycle.Lifecycle.State.DESTROYED;
import static java.util.logging.Logger.getLogger;
import static org.Baqqaproject.Baqqa.android.util.UiUtils.resolveColorAttribute;
import static org.Baqqaproject.Baqqa.android.view.TextSendController.SendState.SENT;
import static org.Baqqaproject.Baqqa.api.messaging.MessagingConstants.MAX_ATTACHMENTS_PER_MESSAGE;

@UiThread
@NotNullByDefault
public class TextAttachmentController extends TextSendController
		implements ImagePreviewListener {

	private static final Logger LOG =
			getLogger(TextAttachmentController.class.getName());

	private final ImagePreview imagePreview;
	private final AttachmentListener attachmentListener;
	private final CompositeSendButton sendButton;
	private final AttachmentManager attachmentManager;

	private final List<Uri> imageUris = new ArrayList<>();
	private boolean loadingUris = false;

	public TextAttachmentController(TextInputView v, ImagePreview imagePreview,
			AttachmentListener attachmentListener,
			AttachmentManager attachmentManager) {
		super(v, attachmentListener, false);
		this.attachmentListener = attachmentListener;
		this.imagePreview = imagePreview;
		this.attachmentManager = attachmentManager;
		this.imagePreview.setImagePreviewListener(this);

		sendButton = (CompositeSendButton) compositeSendButton;
		sendButton.setOnImageClickListener(view -> onImageButtonClicked());
		sendButton.setOnFileClickListener(view -> onFileButtonClicked());
	}

	private void onFileButtonClicked() {
		attachmentListener.onAttachFileClicked();
	}

	@Override
	protected void updateViewState() {
		super.updateViewState();
		if (loadingUris) {
			sendButton.showProgress(true);
		} else if (imageUris.isEmpty()) {
			sendButton.showProgress(false);
			sendButton.showImageButton(textIsEmpty, isSendButtonEnabled());
			// file button stays visible with an empty composer so generic
			// file attachment remains reachable; CompositeSendButton itself
			// hides it if file support is not available
			sendButton.showFileButton(!loadingUris);
		} else {
			sendButton.showProgress(false);
			sendButton.showImageButton(false, isSendButtonEnabled());
			sendButton.showFileButton(false);
		}
	}

	@Override
	protected boolean isTextInputEnabled() {
		return super.isTextInputEnabled() && !loadingUris;
	}

	@Override
	protected boolean isSendButtonEnabled() {
		return super.isSendButtonEnabled() && !loadingUris;
	}

	@Override
	protected boolean isBombVisible() {
		return super.isBombVisible() && (!textIsEmpty || !imageUris.isEmpty());
	}

	@Override
	protected CharSequence getCurrentTextHint() {
		if (imageUris.isEmpty()) {
			return super.getCurrentTextHint();
		} else {
			Context ctx = textInput.getContext();
			return ctx.getString(R.string.image_caption_hint);
		}
	}

	@Override
	public void onSendEvent() {
		if (canSend()) {
			if (loadingUris) throw new AssertionError();
			listener.onSendClick(textInput.getText(),
					attachmentManager.getAttachmentHeadersForSending(),
					expectedTimer).observe(listener, this::onSendStateChanged);
		}
	}

	@Override
	protected void onSendStateChanged(SendState sendState) {
		super.onSendStateChanged(sendState);
		if (sendState == SENT) reset();
	}

	@Override
	protected boolean canSendEmptyText() {
		return !imageUris.isEmpty();
	}

	public void setImagesSupported() {
		sendButton.setImagesSupported();
	}

	public void setFilesSupported() {
		sendButton.setFilesSupported();
	}

	private void onImageButtonClicked() {
		if (!sendButton.hasImageSupport()) {
			Context ctx = imagePreview.getContext();
			Builder builder = new Builder(ctx, R.style.OnboardingDialogTheme);
			builder.setTitle(
					ctx.getString(R.string.dialog_title_no_image_support));
			builder.setMessage(
					ctx.getString(R.string.dialog_message_no_image_support));
			builder.setPositiveButton(R.string.got_it, null);
			builder.show();
			return;
		}
		if (attachmentListener.getLifecycle().getCurrentState() != DESTROYED) {
			attachmentListener.onAttachImageClicked();
		}
	}

	/**
	 * This method must be called at most once per call to
	 * {@link AttachmentListener#onAttachImageClicked()}.
	 * Normally, this is true if called from the launcher equivalent of
	 * {@link Activity#onActivityResult(int, int, Intent)} since this is called
	 * at most once per call to	{@link ActivityResultLauncher#launch(Object)}.
	 */
	@SuppressWarnings("JavadocReference")
	public void onImageReceived(@Nullable List<Uri> newUris) {
		if (newUris == null) return;
		// ignore accidental duplicate/re-entry picker results instead of
		// crashing; the normal single-result flow is unchanged
		if (loadingUris || !imageUris.isEmpty()) {
			LOG.warning("Ignoring duplicate image picker result");
			return;
		}
		onNewUris(false, newUris);
	}

	/**
	 * Called when the user selects one or more generic files/documents.
	 * Files are stored through the same attachment pipeline as images, but no
	 * image thumbnail preview is generated for them.
	 */
	public void onFilesReceived(@Nullable List<Uri> newUris) {
		if (newUris == null) return;
		// ignore accidental duplicate/re-entry picker results instead of
		// crashing; the normal single-result flow is unchanged
		if (loadingUris || !imageUris.isEmpty()) {
			LOG.warning("Ignoring duplicate file picker result");
			return;
		}
		onFilesNewUris(false, newUris);
	}

	private void onNewUris(boolean restart, List<Uri> newUris) {
		if (newUris.isEmpty()) return;
		if (loadingUris) throw new AssertionError();
		if (textIsEmpty) onStartingMessage();
		loadingUris = true;
		if (newUris.size() > MAX_ATTACHMENTS_PER_MESSAGE) {
			newUris = newUris.subList(0, MAX_ATTACHMENTS_PER_MESSAGE);
			attachmentListener.onTooManyAttachments();
		}
		imageUris.addAll(newUris);
		updateViewState();
		List<ImagePreviewItem> items = ImagePreviewItem.fromUris(imageUris);
		imagePreview.showPreview(items);
		// store attachments and show preview when successful
		LiveData<AttachmentResult> result =
				attachmentManager.storeAttachments(imageUris, restart);
		result.observe(attachmentListener, new Observer<AttachmentResult>() {
			@Override
			public void onChanged(@Nullable AttachmentResult attachmentResult) {
				if (attachmentResult == null) {
					// The fresh LiveData was deliberately set to null.
					// This means that we can stop observing it.
					result.removeObserver(this);
				} else {
					boolean noError = onNewAttachmentItemResults(
							attachmentResult.getItemResults());
					if (noError && attachmentResult.isFinished()) {
						onAllAttachmentsCreated();
						result.removeObserver(this);
					}
				}
			}
		});
	}

	private boolean onNewAttachmentItemResults(
			Collection<AttachmentItemResult> itemResults) {
		if (!loadingUris) throw new AssertionError();
		for (AttachmentItemResult result : itemResults) {
			if (result.hasError()) {
				onError(result.getErrorMsg());
				return false;
			} else {
				imagePreview.loadPreviewImage(result);
			}
		}
		return true;
	}

	/**
	 * Stores generic files through the same attachment pipeline as images but
	 * without generating a thumbnail preview (deferred to a later phase).
	 */
	private void onFilesNewUris(boolean restart, List<Uri> newUris) {
		if (newUris.isEmpty()) return;
		if (loadingUris) throw new AssertionError();
		if (textIsEmpty) onStartingMessage();
		loadingUris = true;
		if (newUris.size() > MAX_ATTACHMENTS_PER_MESSAGE) {
			newUris = newUris.subList(0, MAX_ATTACHMENTS_PER_MESSAGE);
			attachmentListener.onTooManyAttachments();
		}
		imageUris.addAll(newUris);
		updateViewState();
		LiveData<AttachmentResult> result =
				attachmentManager.storeAttachments(imageUris, restart);
		result.observe(attachmentListener, new Observer<AttachmentResult>() {
			@Override
			public void onChanged(@Nullable AttachmentResult attachmentResult) {
				if (attachmentResult == null) {
					result.removeObserver(this);
				} else {
					boolean noError = onFilesNewAttachmentItemResults(
							attachmentResult.getItemResults());
					if (noError && attachmentResult.isFinished()) {
						onAllAttachmentsCreated();
						result.removeObserver(this);
					}
				}
			}
		});
	}

	private boolean onFilesNewAttachmentItemResults(
			Collection<AttachmentItemResult> itemResults) {
		if (!loadingUris) throw new AssertionError();
		for (AttachmentItemResult result : itemResults) {
			if (result.hasError()) {
				onError(result.getErrorMsg());
				return false;
			}
		}
		return true;
	}

	private void onAllAttachmentsCreated() {
		if (!loadingUris) throw new AssertionError();
		loadingUris = false;
		updateViewState();
	}

	private void reset() {
		// hide image layout
		imagePreview.setVisibility(GONE);
		// reset image URIs
		imageUris.clear();
		// definitely not loading anymore
		loadingUris = false;
		// show the image button again, so images can get attached
		updateViewState();
	}

	@Override
	public Parcelable onSaveInstanceState(@Nullable Parcelable superState) {
		SavedState state =
				new SavedState(superState == null ? EMPTY_STATE : superState);
		state.imageUris = imageUris;
		return state;
	}

	@Override
	@Nullable
	public Parcelable onRestoreInstanceState(Parcelable inState) {
		SavedState state = (SavedState) inState;
		if (!imageUris.isEmpty()) throw new AssertionError();
		if (state.imageUris != null) onNewUris(true, state.imageUris);
		return state.getSuperState();
	}

	@UiThread
	private void onError(@Nullable String errorMsg) {
		if (errorMsg == null) {
			errorMsg = imagePreview.getContext()
					.getString(R.string.image_attach_error);
		}
		Toast.makeText(textInput.getContext(), errorMsg, LENGTH_LONG).show();
		onCancel();
	}

	@Override
	public void onCancel() {
		textInput.clearText();
		attachmentManager.cancel();
		reset();
	}

	public void showImageOnboarding(Activity activity) {
		int color = resolveColorAttribute(activity, R.attr.colorControlNormal);
		Drawable drawable = VectorDrawableCompat
				.create(activity.getResources(), R.drawable.ic_image, null);
		new MaterialTapTargetPrompt.Builder(activity,
				R.style.OnboardingDialogTheme).setTarget(sendButton)
				.setPrimaryText(R.string.dialog_title_image_support)
				.setSecondaryText(R.string.dialog_message_image_support)
				.setBackgroundColour(getColor(activity, R.color.baqqa_primary))
				.setIconDrawable(drawable)
				.setIconDrawableColourFilter(color)
				.show();
	}

	private static class SavedState extends AbsSavedState {

		@Nullable
		private List<Uri> imageUris;

		private SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			//noinspection unchecked
			imageUris = in.readArrayList(Uri.class.getClassLoader());
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeList(imageUris);
		}

		public static final Creator<SavedState> CREATOR =
				new Creator<SavedState>() {
					@Override
					public SavedState createFromParcel(Parcel in) {
						return new SavedState(in);
					}

					@Override
					public SavedState[] newArray(int size) {
						return new SavedState[size];
					}
				};
	}

	@UiThread
	public interface AttachmentListener extends SendListener {

		void onAttachImageClicked();

		void onAttachFileClicked();

		void onTooManyAttachments();
	}
}




