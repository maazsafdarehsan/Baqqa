package org.Baqqaproject.Baqqa.android.voice;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import org.briarproject.nullsafety.NotNullByDefault;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;

/**
 * Thin wrapper around {@link MediaPlayer} for playing a single voice note.
 * <p>
 * Must be used on the UI thread. The player is always released via
 * {@link #release()}. Missing, corrupt and unplayable audio is reported
 * through {@link Listener#onError()} instead of crashing.
 */
@UiThread
@NotNullByDefault
public class AudioPlayer {

	private static final Logger LOG =
			getLogger(AudioPlayer.class.getName());

	@Nullable
	private MediaPlayer player;
	@Nullable
	private Listener listener;
	private boolean prepared = false;

	/**
	 * Starts playing the given file. Any previously playing audio is stopped
	 * and released first.
	 */
	public void play(Context ctx, File file, Listener listener) {
		release();
		this.listener = listener;
		// create the player and register all listeners BEFORE preparation;
		// MediaPlayer.create() prepares synchronously, which means a listener
		// registered afterwards would never fire
		MediaPlayer player = new MediaPlayer();
		this.player = player;
		player.setLooping(false);
		player.setOnErrorListener((mp, what, extra) -> {
			notifyError();
			release();
			return true;
		});
		player.setOnPreparedListener(mp -> {
			prepared = true;
			try {
				mp.start();
				if (this.listener != null) this.listener.onPlaying();
			} catch (IllegalStateException e) {
				notifyError();
			}
		});
		player.setOnSeekCompleteListener(mp -> {
			if (listener != null) listener.onSeekComplete();
		});
		player.setOnCompletionListener(mp -> {
			// keep the player prepared and loaded at the end position so the
			// user can seek backwards and replay; the listener decides when
			// the playback session is really finished
			if (listener != null) listener.onCompleted();
		});
		try {
			player.setDataSource(ctx, Uri.fromFile(file));
			player.prepareAsync();
		} catch (IOException | RuntimeException e) {
			LOG.log(WARNING, "Could not prepare media player", e);
			notifyError();
			release();
		}
	}

	public boolean isPlaying() {
		MediaPlayer player = this.player;
		return player != null && prepared && player.isPlaying();
	}

	public void pause() {
		MediaPlayer player = this.player;
		if (player != null && prepared && player.isPlaying()) {
			player.pause();
			if (listener != null) listener.onPaused();
		}
	}

	public void resume() {
		MediaPlayer player = this.player;
		if (player != null && prepared) {
			try {
				// if playback finished previously, restart from the beginning
				int duration = getDurationMillis();
				if (duration > 0
						&& getCurrentPositionMillis() >= duration) {
					player.seekTo(0);
				}
				player.start();
				if (listener != null) listener.onPlaying();
			} catch (IllegalStateException e) {
				notifyError();
			}
		}
	}

	/**
	 * Seeks the loaded audio to the given position in milliseconds. If the
	 * target is at or beyond the end of the audio, the position moves to the
	 * total duration. Playback state (playing/paused) is preserved; a seek
	 * to the end while playing triggers the normal completion callback.
	 * Does nothing if no audio is prepared.
	 */
	public void seekTo(int msec) {
		MediaPlayer player = this.player;
		if (player == null || !prepared) return;
		try {
			int duration = getDurationMillis();
			if (duration > 0 && msec > duration) msec = duration;
			if (msec < 0) msec = 0;
			player.seekTo(msec);
		} catch (IllegalStateException e) {
			LOG.log(WARNING, "Could not seek media player", e);
		}
	}

	/** Current playback position in milliseconds, or -1 if not playing. */
	public int getCurrentPositionMillis() {
		MediaPlayer player = this.player;
		if (player == null || !prepared) return -1;
		try {
			return player.getCurrentPosition();
		} catch (IllegalStateException e) {
			return -1;
		}
	}

	/** Total duration in milliseconds, or -1 if unknown. */
	public int getDurationMillis() {
		MediaPlayer player = this.player;
		if (player == null || !prepared) return -1;
		try {
			int d = player.getDuration();
			return d > 0 ? d : -1;
		} catch (IllegalStateException e) {
			return -1;
		}
	}

	/** Releases the player and any native resources. Safe to call anytime. */
	public void release() {
		MediaPlayer player = this.player;
		this.player = null;
		this.listener = null;
		this.prepared = false;
		if (player != null) {
			try {
				player.release();
			} catch (RuntimeException e) {
				LOG.log(WARNING, "Could not release media player", e);
			}
		}
	}

	private void notifyError() {
		// take a local reference so a listener released in error still fires
		Listener listener = this.listener;
		this.listener = null;
		if (listener != null) listener.onError();
	}

	@UiThread
	public interface Listener {

		/** Playback started or resumed. */
		void onPlaying();

		/** Playback was paused by the user. */
		void onPaused();

		/** Playback finished or was stopped. */
		void onStopped();

		/**
		 * Playback reached the end of the audio. The player remains loaded
		 * at the end position so the user can seek backwards and replay.
		 */
		void onCompleted();

		/** A seek operation initiated via {@link #seekTo(int)} completed. */
		void onSeekComplete();

		/** The audio could not be played. */
		void onError();
	}

}
