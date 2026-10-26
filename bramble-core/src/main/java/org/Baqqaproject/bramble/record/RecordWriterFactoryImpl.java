package org.Baqqaproject.bramble.record;

import org.Baqqaproject.bramble.api.record.RecordWriter;
import org.Baqqaproject.bramble.api.record.RecordWriterFactory;

import java.io.OutputStream;

class RecordWriterFactoryImpl implements RecordWriterFactory {

	@Override
	public RecordWriter createRecordWriter(OutputStream out) {
		return new RecordWriterImpl(out);
	}
}



