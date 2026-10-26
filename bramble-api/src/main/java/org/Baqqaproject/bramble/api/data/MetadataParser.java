package org.Baqqaproject.bramble.api.data;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.db.Metadata;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface MetadataParser {

	BdfDictionary parse(Metadata m) throws FormatException;
}




