package org.Baqqaproject.bramble.plugin.file;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.plugin.file.RemovableDriveTask;
import org.Baqqaproject.bramble.api.properties.TransportProperties;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface RemovableDriveTaskFactory {

	RemovableDriveTask createReader(RemovableDriveTaskRegistry registry,
			TransportProperties p);

	RemovableDriveTask createWriter(RemovableDriveTaskRegistry registry,
			ContactId c, TransportProperties p);
}




