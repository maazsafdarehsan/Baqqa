package org.Baqqaproject.bramble.sync;

import org.Baqqaproject.bramble.api.record.RecordWriter;
import org.Baqqaproject.bramble.api.record.RecordWriterFactory;
import org.Baqqaproject.bramble.api.sync.MessageFactory;
import org.Baqqaproject.bramble.api.sync.SyncRecordWriter;
import org.Baqqaproject.bramble.api.sync.SyncRecordWriterFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.OutputStream;

import javax.inject.Inject;

@NotNullByDefault
class SyncRecordWriterFactoryImpl implements SyncRecordWriterFactory {

	private final MessageFactory messageFactory;
	private final RecordWriterFactory recordWriterFactory;

	@Inject
	SyncRecordWriterFactoryImpl(MessageFactory messageFactory,
			RecordWriterFactory recordWriterFactory) {
		this.messageFactory = messageFactory;
		this.recordWriterFactory = recordWriterFactory;
	}

	@Override
	public SyncRecordWriter createRecordWriter(OutputStream out) {
		RecordWriter writer = recordWriterFactory.createRecordWriter(out);
		return new SyncRecordWriterImpl(messageFactory, writer);
	}
}




