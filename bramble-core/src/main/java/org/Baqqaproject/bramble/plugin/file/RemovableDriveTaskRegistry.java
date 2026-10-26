package org.Baqqaproject.bramble.plugin.file;

import org.Baqqaproject.bramble.api.plugin.file.RemovableDriveTask;
import org.briarproject.nullsafety.NotNullByDefault;

@Deprecated // We can simply remove tasks when they finish
@NotNullByDefault
interface RemovableDriveTaskRegistry {

	void removeReader(RemovableDriveTask task);

	void removeWriter(RemovableDriveTask task);
}




