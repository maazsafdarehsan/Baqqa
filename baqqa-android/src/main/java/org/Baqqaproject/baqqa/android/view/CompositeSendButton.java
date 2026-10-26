package org.Baqqaproject.Baqqa.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.Baqqaproject.Baqqa.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static java.util.Objects.requireNonNull;

public class CompositeSendButton extends FrameLayout {

	private final AppCompatImageButton sendButton, imageButton, fileButton;
	private final ImageView bombBadge;
	private final ProgressBar progressBar;

	private boolean hasImageSupport = false;
	private boolean hasFileSupport = false;

	public CompositeSendButton(@NonNull Context context,
			@Nullable AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) requireNonNull(
				context.getSystemService(LAYOUT_INFLATER_SERVICE));
		inflater.inflate(R.layout.view_composite_send_button, this, true);

		sendButton = findViewById(R.id.sendButton);
		imageButton = findViewById(R.id.imageButton);
		fileButton = findViewById(R.id.fileButton);
		bombBadge = findViewById(R.id.bombBadge);
		progressBar = findViewById(R.id.progressBar);
	}

	@Override
	public void setEnabled(boolean enabled) {
		setSendEnabled(enabled);
	}

	@Override
	public void setOnClickListener(@Nullable View.OnClickListener l) {
		setOnSendClickListener(l);
	}

	public void setOnSendClickListener(@Nullable OnClickListener l) {
		sendButton.setOnClickListener(l);
	}

	public void setSendEnabled(boolean enabled) {
		sendButton.setEnabled(enabled);
	}

	public void setOnImageClickListener(@Nullable OnClickListener l) {
		imageButton.setOnClickListener(l);
	}

	public void setOnFileClickListener(@Nullable OnClickListener l) {
		fileButton.setOnClickListener(l);
	}

	/**
	 * By default, image support is disabled.
	 * Once you know that it is supported in the current context,
	 * call this method to enable it.
	 */
	public void setImagesSupported() {
		hasImageSupport = true;
		imageButton.setImageResource(R.drawable.ic_image);
	}

	public boolean hasImageSupport() {
		return hasImageSupport;
	}

	/**
	 * By default, file/documents support is disabled. Once you know that it
	 * is supported in the current context, call this method to enable it.
	 */
	public void setFilesSupported() {
		hasFileSupport = true;
		fileButton.setImageResource(R.drawable.ic_attach_file);
	}

	public boolean hasFileSupport() {
		return hasFileSupport;
	}

	public void setBombVisible(boolean visible) {
		bombBadge.setVisibility(visible ? VISIBLE : INVISIBLE);
	}

	public void showImageButton(boolean showImageButton, boolean sendEnabled) {
		if (showImageButton) {
			imageButton.setVisibility(VISIBLE);
			sendButton.setEnabled(false);
			sendButton.clearAnimation();
			sendButton.animate().alpha(0f).withEndAction(() -> {
				sendButton.setVisibility(INVISIBLE);
				imageButton.setEnabled(true);
			}).start();
			imageButton.clearAnimation();
			imageButton.animate().alpha(1f).start();
		} else {
			sendButton.setVisibility(VISIBLE);
			// enable/disable buttons right away to allow fast sending
			sendButton.setEnabled(sendEnabled);
			imageButton.setEnabled(false);
			sendButton.clearAnimation();
			sendButton.animate().alpha(1f).start();
			imageButton.clearAnimation();
			imageButton.animate().alpha(0f).withEndAction(() ->
					imageButton.setVisibility(INVISIBLE)
			).start();
		}
	}

	public void showFileButton(boolean showFileButton) {
		if (showFileButton && hasFileSupport) {
			fileButton.setVisibility(VISIBLE);
			fileButton.setEnabled(true);
		} else {
			fileButton.setVisibility(INVISIBLE);
			fileButton.setEnabled(false);
		}
	}

	public void showProgress(boolean show) {
		sendButton.setVisibility(show ? INVISIBLE : VISIBLE);
		imageButton.setVisibility(show ? INVISIBLE : VISIBLE);
		fileButton.setEnabled(!show && hasFileSupport);
		progressBar.setVisibility(show ? VISIBLE : INVISIBLE);
	}

}



