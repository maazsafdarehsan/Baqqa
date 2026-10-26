package org.Baqqaproject.Baqqa.api.sharing;

import org.Baqqaproject.bramble.api.Nameable;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface Shareable extends Nameable {

	GroupId getId();

}




