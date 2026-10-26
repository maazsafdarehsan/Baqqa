package org.Baqqaproject.Baqqa.android.voice;

import org.briarproject.nullsafety.NotNullByDefault;

/**
 * Pure, device-independent helpers for voice notes. Kept free of Android
 * framework dependencies so the logic can be unit-tested on the JVM.
 */
@NotNullByDefault
public final class VoiceNote {

	/**
	 * MIME type of recordings produced by {@link VoiceNoteRecorder}
	 * (AAC audio in an MPEG-4 container).
	 */
	public static final String MIME_TYPE = "audio/mp4";

	/** Display name stored as attachment metadata. */
	public static final String FILE_NAME = "Voice Message";

	/** Sub-directory of the cache dir in which recordings are stored. */
	public static final String DIRECTORY = "voice_messages";

	private static final String PREFIX = "voice_note_";
	private static final String SUFFIX = ".m4a";

	private VoiceNote() {
	}

	/**
	 * Returns the file a new recording should be written to, in the given
	 * directory, named after the given timestamp.
	 */
	public static java.io.File getOutputFile(java.io.File dir, long timestamp) {
		return new java.io.File(dir, PREFIX + timestamp + SUFFIX);
	}

	/**
	 * Formats a duration in seconds as "m:ss", e.g. {@code 0:07} or
	 * {@code 1:23}. Used for the compact duration label on voice clips.
	 */
	public static String formatSeconds(long seconds) {
		if (seconds < 0) seconds = 0;
		long minutes = seconds / 60;
		long secs = seconds % 60;
		return minutes + ":" + (secs < 10 ? "0" : "") + secs;
	}

}
