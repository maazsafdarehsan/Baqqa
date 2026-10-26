package org.Baqqaproject.bramble.record;

import org.Baqqaproject.bramble.api.record.RecordReaderFactory;
import org.Baqqaproject.bramble.api.record.RecordWriterFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class RecordModule {

	@Provides
	RecordReaderFactory provideRecordReaderFactory() {
		return new RecordReaderFactoryImpl();
	}

	@Provides
	RecordWriterFactory provideRecordWriterFactory() {
		return new RecordWriterFactoryImpl();
	}
}



