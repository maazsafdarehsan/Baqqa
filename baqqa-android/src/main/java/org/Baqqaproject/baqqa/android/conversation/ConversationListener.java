package org.Baqqaproject.Baqqa.android.conversation;

import android.view.View;

import org.Baqqaproject.Baqqa.android.attachment.AttachmentItem;
import org.briarproject.nullsafety.NotNullByDefault;

import androidx.annotation.UiThread;

@UiThread
@NotNullByDefault
interface ConversationListener {

	void respondToRequest(ConversationRequestItem item, boolean accept);

	void openRequestedShareable(ConversationRequestItem item);

	void onAttachmentClicked(View view, ConversationMessageItem messageItem,
			AttachmentItem attachmentItem);

	/**
	 * Returns true if the given audio attachment is currently playing, used
	 * to show a pause control on voice-note chips.
	 */
	boolean isAudioPlaying(AttachmentItem item);

	/**
	 * Returns a compact duration/progress label for the given audio
	 * attachment, or an empty string if not known.
	 */
	String getAudioDurationText(AttachmentItem item);

	/**
	 * Returns the playback progress of the given audio attachment in the
	 * range [0, 1000], or 0 if not playing.
	 */
	int getAudioProgress(AttachmentItem item);

	/**
	 * Returns the total duration of the currently loaded audio in
	 * milliseconds, or -1 if unknown. Used while the user drags the
	 * seekable timeline.
	 */
	int getAudioDurationMillis(AttachmentItem item);

	/**
	 * Called when the user starts dragging the voice timeline; playback
	 * position refreshes are paused until the seek is committed.
	 */
	void onAudioSeekStarted(AttachmentItem item);

	/** Called while the user drags the voice timeline, in [0, 1000]. */
	void onAudioSeekPreview(AttachmentItem item, int progress);

	/**
	 * Called when the user releases the voice timeline; the audio is
	 * seeked to the given position in [0, 1000].
	 */
	void onAudioSeekCommitted(AttachmentItem item, int progress);

	void onAutoDeleteTimerNoticeClicked();

	void onLinkClick(String url);

}




