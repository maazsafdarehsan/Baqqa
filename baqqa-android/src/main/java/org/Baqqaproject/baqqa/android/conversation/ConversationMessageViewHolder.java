package org.Baqqaproject.Baqqa.android.conversation;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.Baqqaproject.Baqqa.R;
import org.Baqqaproject.Baqqa.android.attachment.AttachmentItem;
import org.Baqqaproject.Baqqa.android.voice.VoiceNote;
import org.briarproject.nullsafety.NotNullByDefault;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool;

import static androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT;
import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.widget.ImageViewCompat.setImageTintList;

@UiThread
@NotNullByDefault
class ConversationMessageViewHolder extends ConversationItemViewHolder {

	private final ImageAdapter adapter;
	private final RecyclerView imageList;
	private final ViewGroup statusLayout;
	private final LinearLayout fileList;
	private final int timeColor, timeColorBubble;
	// views of the currently bound voice chip (if any), updated directly
	// during playback so the tick does not have to rebind the ViewHolder
	@Nullable
	private AttachmentItem voiceAttachment;
	@Nullable
	private SeekBar voiceSeekBar;
	@Nullable
	private TextView voiceDuration;
	private final ConstraintSet textConstraints = new ConstraintSet();
	private final ConstraintSet imageConstraints = new ConstraintSet();
	private final ConstraintSet imageTextConstraints = new ConstraintSet();

	ConversationMessageViewHolder(View v, ConversationListener listener,
			boolean isIncoming, RecycledViewPool imageViewPool,
			ImageItemDecoration imageItemDecoration) {
		super(v, listener, isIncoming);
		statusLayout = v.findViewById(R.id.statusLayout);

		// image list
		imageList = v.findViewById(R.id.imageList);
		imageList.setRecycledViewPool(imageViewPool);
		adapter = new ImageAdapter(v.getContext(), listener);
		imageList.setAdapter(adapter);
		imageList.addItemDecoration(imageItemDecoration);

		// file list container
		fileList = v.findViewById(R.id.fileList);

		// remember original status text color
		timeColor = time.getCurrentTextColor();
		timeColorBubble =
				getColor(v.getContext(), R.color.msg_status_bubble_foreground);

		// clone constraint sets from layout files
		textConstraints.clone(v.getContext(),
				R.layout.list_item_conversation_msg_in_content);
		imageConstraints.clone(v.getContext(),
				R.layout.list_item_conversation_msg_image);
		imageTextConstraints.clone(v.getContext(),
				R.layout.list_item_conversation_msg_image_text);

		// in/out are different layouts, so we need to do this only once
		textConstraints
				.setHorizontalBias(R.id.statusLayout, isIncoming() ? 1 : 0);
		imageConstraints
				.setHorizontalBias(R.id.statusLayout, isIncoming() ? 1 : 0);
		imageTextConstraints
				.setHorizontalBias(R.id.statusLayout, isIncoming() ? 1 : 0);
	}

	@Override
	void bind(ConversationItem conversationItem, boolean selected) {
		super.bind(conversationItem, selected);
		ConversationMessageItem item =
				(ConversationMessageItem) conversationItem;
		if (item.getAttachments().isEmpty()) {
			bindTextItem();
		} else if (item.getAttachments().get(0).isImage()) {
			bindImageItem(item);
		} else {
			bindFileItem(item);
		}
	}

	private void bindTextItem() {
		clearVoiceChip();
		resetStatusLayoutForText();
		textConstraints.applyTo(layout);
		adapter.clear();
		fileList.setVisibility(View.GONE);
	}

	private void bindImageItem(ConversationMessageItem item) {
		clearVoiceChip();
		// hide file list, show image list
		fileList.setVisibility(View.GONE);
		imageList.setVisibility(View.VISIBLE);

		ConstraintSet constraintSet;
		if (item.getText() == null) {
			statusLayout.setBackgroundResource(R.drawable.msg_status_bubble);
			time.setTextColor(timeColorBubble);
			setImageTintList(bomb, ColorStateList.valueOf(timeColorBubble));
			constraintSet = imageConstraints;
		} else {
			resetStatusLayoutForText();
			constraintSet = imageTextConstraints;
		}

		if (item.getAttachments().size() == 1) {
			// apply image size constraints for a single image
			AttachmentItem attachment = item.getAttachments().get(0);
			int width = attachment.getThumbnailWidth();
			int height = attachment.getThumbnailHeight();
			constraintSet.constrainWidth(R.id.imageList, width);
			constraintSet.constrainHeight(R.id.imageList, height);
		} else {
			// bubble adapts to size of image list
			constraintSet.constrainWidth(R.id.imageList, WRAP_CONTENT);
			constraintSet.constrainHeight(R.id.imageList, WRAP_CONTENT);
		}
		constraintSet.applyTo(layout);
		adapter.setConversationItem(item);
	}

	private void bindFileItem(ConversationMessageItem item) {
		clearVoiceChip();
		resetStatusLayoutForText();
		textConstraints.applyTo(layout);
		adapter.clear();

		// hide image list, show file list
		imageList.setVisibility(View.GONE);
		fileList.setVisibility(View.VISIBLE);
		fileList.removeAllViews();

		// inflate a chip for each non-image attachment
		for (AttachmentItem attachment : item.getAttachments()) {
			if (attachment.isAudio()) {
				fileList.addView(bindVoiceChip(item, attachment));
			} else {
				fileList.addView(bindFileChip(item, attachment));
			}
		}
	}

	private View bindVoiceChip(ConversationMessageItem item,
			AttachmentItem attachment) {
		View chip = LayoutInflater.from(fileList.getContext())
				.inflate(R.layout.list_item_voice, fileList, false);
		AppCompatImageButton playButton =
				chip.findViewById(R.id.playButton);
		SeekBar progressBar = chip.findViewById(R.id.progressBar);
		TextView duration = chip.findViewById(R.id.duration);

		boolean playing = listener.isAudioPlaying(attachment);
		// only the play/pause button toggles playback; the seek bar shows
		// the real playback position and supports tapping/dragging
		playButton.setImageResource(
				playing ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
		playButton.setContentDescription(fileList.getContext().getString(
				playing ? R.string.voice_pause : R.string.voice_play));
		progressBar.setProgress(listener.getAudioProgress(attachment));
		duration.setText(listener.getAudioDurationText(attachment));

		// remember this chip so playback ticks can update it directly
		voiceAttachment = attachment;
		voiceSeekBar = progressBar;
		voiceDuration = duration;

		playButton.setOnClickListener(v ->
				listener.onAttachmentClicked(v, item, attachment));

		progressBar.setOnSeekBarChangeListener(
				new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (!fromUser) return;
						listener.onAudioSeekPreview(attachment, progress);
						// update elapsed time immediately while dragging
						int d = listener.getAudioDurationMillis(attachment);
						if (d > 0) {
							long posMs = progress * (long) d / 1000L;
							duration.setText(VoiceNote.formatSeconds(
									(int) (posMs / 1000)) + " / " +
									VoiceNote.formatSeconds(d / 1000));
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						listener.onAudioSeekStarted(attachment);
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						listener.onAudioSeekCommitted(attachment,
								seekBar.getProgress());
					}
				});
		return chip;
	}

	/**
	 * Updates the bound voice chip's seek bar and elapsed time from the
	 * authoritative playback state, without rebinding the ViewHolder.
	 * Only advances while this attachment is actually playing, so recycled
	 * or off-screen holders are never updated incorrectly.
	 */
	@UiThread
	void updateVoiceProgress() {
		AttachmentItem a = voiceAttachment;
		if (a == null || voiceSeekBar == null || voiceDuration == null) {
			return;
		}
		if (!listener.isAudioPlaying(a)) return;
		voiceSeekBar.setProgress(listener.getAudioProgress(a));
		voiceDuration.setText(listener.getAudioDurationText(a));
	}

	private void clearVoiceChip() {
		voiceAttachment = null;
		voiceSeekBar = null;
		voiceDuration = null;
	}

	private View bindFileChip(ConversationMessageItem item,
			AttachmentItem attachment) {
		View chip = LayoutInflater.from(fileList.getContext())
				.inflate(R.layout.list_item_file, fileList, false);
		TextView fileName = chip.findViewById(R.id.fileName);
		TextView fileSize = chip.findViewById(R.id.fileSize);

		String name = attachment.getFileName();
		if (name == null) name = attachment.getExtension();
		if (name == null) name = "";
		fileName.setText(name);

		long totalSize = attachment.getTotalSize();
		String sizeText;
		if (totalSize > 0) {
			sizeText = android.text.format.Formatter.formatShortFileSize(
					fileList.getContext(), totalSize);
			fileSize.setText(sizeText);
			fileSize.setVisibility(View.VISIBLE);
		} else {
			sizeText = "";
			fileSize.setVisibility(View.GONE);
		}

		// accessibility: announce file name and size
		chip.setContentDescription(
				fileList.getContext().getString(
						R.string.file_chip_content_description,
						name, sizeText));

		chip.setContentDescription(
				fileList.getContext().getString(
						R.string.file_chip_content_description,
						name, fileSize.getText()));
		chip.setOnClickListener(v ->
				listener.onAttachmentClicked(v, item, attachment));
		return chip;
	}

	private void resetStatusLayoutForText() {
		statusLayout.setBackgroundResource(0);
		// also reset padding (the background drawable defines some)
		statusLayout.setPadding(0, 0, 0, 0);
		time.setTextColor(timeColor);
		setImageTintList(bomb, ColorStateList.valueOf(timeColor));
	}

}




