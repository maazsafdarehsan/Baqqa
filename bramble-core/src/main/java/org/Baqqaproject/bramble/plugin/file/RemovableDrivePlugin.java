package org.Baqqaproject.bramble.plugin.file;

import org.Baqqaproject.bramble.api.plugin.PluginCallback;
import org.Baqqaproject.bramble.api.properties.TransportProperties;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.concurrent.Immutable;

import static org.Baqqaproject.bramble.api.plugin.file.RemovableDriveConstants.PROP_PATH;
import static org.Baqqaproject.bramble.util.StringUtils.isNullOrEmpty;

@Immutable
@NotNullByDefault
class RemovableDrivePlugin extends AbstractRemovableDrivePlugin {

	RemovableDrivePlugin(PluginCallback callback, long maxLatency) {
		super(callback, maxLatency);
	}

	@Override
	InputStream openInputStream(TransportProperties p) throws IOException {
		String path = p.get(PROP_PATH);
		if (isNullOrEmpty(path)) throw new IllegalArgumentException();
		return new FileInputStream(path);
	}

	@Override
	OutputStream openOutputStream(TransportProperties p) throws IOException {
		String path = p.get(PROP_PATH);
		if (isNullOrEmpty(path)) throw new IllegalArgumentException();
		return new FileOutputStream(path);
	}
}




