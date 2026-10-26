package org.Baqqaproject.bramble.api.reporting;

import org.Baqqaproject.bramble.api.crypto.PublicKey;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.File;

@NotNullByDefault
public interface DevConfig {

	PublicKey getDevPublicKey();

	String getDevOnionAddress();

	File getReportDir();

	File getLogcatFile();
}




