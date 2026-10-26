package org.Baqqaproject.bramble.record;

import org.Baqqaproject.bramble.api.record.RecordReader;
import org.Baqqaproject.bramble.api.record.RecordReaderFactory;

import java.io.InputStream;

class RecordReaderFactoryImpl implements RecordReaderFactory {

	@Override
	public RecordReader createRecordReader(InputStream in) {
		return new RecordReaderImpl(in);
	}
}



