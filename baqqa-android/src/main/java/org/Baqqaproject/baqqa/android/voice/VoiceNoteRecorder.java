package org.Baqqaproject.Baqqa.android.voice;

import android.media.MediaRecorder;

import org.briarproject.nullsafety.NotNullByDefault;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;

/**
 * Records a voice note to a file in the app cache using {@link MediaRecorder}.
 * <p>
 * All methods must be called on the UI thread. The recorder is fully released
 * on {@link #stop()}, {@link #cancel()} and {@link #release()}, and the 
 * temporary output file is deleted on cancel, so no recordings or native
 * resources are left behind.
 */
@UiThread
@NotNullByDefault
public class VoiceNoteRecorder {

	private static final Logger LOG =
			getLogger(VoiceNoteRecorder.class.getName());

	private final File outputDir;
	@Nullable
	private MediaRecorder recorder;
	@Nullable
	private File outputFile;
	private long startedAt = -1;
	private boolean recording = false;

	public VoiceNoteRecorder(File cacheDir) {
		outputDir = new File(cacheDir, VoiceNote.DIRECTORY);
		if (!outputDir.exists() && !outputDir.mkdirs()) {
			LOG.warning("Could not create voice note directory");
		}
	}

	/**
	 * Starts a new recording. Any previous recording and its file are
	 * discarded first.
	 *
	 * @throws IOException if the recorder could not be initialised or started.
	 */
	public void start() throws IOException {
		release();
		long timestamp = System.currentTimeMillis();
		File file = VoiceNote.getOutputFile(outputDir, timestamp);
		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		recorder.setOutputFile(file.getAbsolutePath());
		try {
			recorder.prepare();
		} catch (IOException e) {
			recorder.release();
			throw e;
		}
		recorder.start();
		this.recorder = recorder;
		this.outputFile = file;
		this.startedAt = System.currentTimeMillis();
		this.recording = true;
	}

	public boolean isRecording() {
		return recording;
	}

	/** Duration of the current recording in milliseconds, or -1 if none. */
	public long getDurationMillis() {
		if (!recording || startedAt == -1) return -1;
		return System.currentTimeMillis() - startedAt;
	}

	/**
	 * Stops the current recording and returns the recorded file, or null if
	 * the recording was too short to be useful. The recorder is always
	 * released. The returned file is owned by the caller.
	 */
	@Nullable
	public File stop() {
		if (!recording) return null;
		MediaRecorder recorder = this.recorder;
		File file = this.outputFile;
		long duration = getDurationMillis();
		boolean shortRecording = duration < 1000;
		release();
		if (shortRecording || file == null) {
			if (file != null && !file.delete()) {
				LOG.warning("Could not delete short recording");
			}
			LOG.info("Discarding short or empty recording");
			return null;
		}
		return file;
	}

	/**
	 * Cancels the current recording, if any, releasing the recorder and
	 * deleting any temporary output file.
	 */
	public void cancel() {
		File file = this.outputFile;
		release();
		if (file != null && !file.delete()) {
			LOG.warning("Could not delete cancelled recording");
		}
	}

	/**
	 * Releases the recorder if one is active and any native resources.
	 * Does not delete the output file (use {@link #cancel()} for that).
	 */
	public void release() {
		recording = false;
		MediaRecorder recorder = this.recorder;
		if (recorder != null) {
			try {
				recorder.stop();
			} catch (RuntimeException e) {
				// stop() can throw if no valid data was captured
				LOG.log(WARNING, "Could not stop recorder", e);
			}
			recorder.release();
			this.recorder = null;
		}
		this.outputFile = null;
		this.startedAt = -1;
	}

}
