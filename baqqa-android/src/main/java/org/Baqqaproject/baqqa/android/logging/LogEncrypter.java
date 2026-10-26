package org.Baqqaproject.Baqqa.android.logging;

import org.Baqqaproject.bramble.util.AndroidUtils;
import org.briarproject.nullsafety.NotNullByDefault;

import androidx.annotation.Nullable;

@NotNullByDefault
public interface LogEncrypter {
	/**
	 * Writes encrypted log records to {@link AndroidUtils#getLogcatFile}
	 * and returns the encryption key if everything went fine.
	 */
	@Nullable
	byte[] encryptLogs();
}




