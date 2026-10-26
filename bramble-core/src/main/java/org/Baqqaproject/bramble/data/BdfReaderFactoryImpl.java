package org.Baqqaproject.bramble.data;

import org.Baqqaproject.bramble.api.data.BdfReader;
import org.Baqqaproject.bramble.api.data.BdfReaderFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.InputStream;

import javax.annotation.concurrent.Immutable;

import static org.Baqqaproject.bramble.api.data.BdfReader.DEFAULT_MAX_BUFFER_SIZE;
import static org.Baqqaproject.bramble.api.data.BdfReader.DEFAULT_NESTED_LIMIT;

@Immutable
@NotNullByDefault
class BdfReaderFactoryImpl implements BdfReaderFactory {

	@Override
	public BdfReader createReader(InputStream in) {
		return new BdfReaderImpl(in, DEFAULT_NESTED_LIMIT,
				DEFAULT_MAX_BUFFER_SIZE, true);
	}

	@Override
	public BdfReader createReader(InputStream in, boolean canonical) {
		return new BdfReaderImpl(in, DEFAULT_NESTED_LIMIT,
				DEFAULT_MAX_BUFFER_SIZE, canonical);
	}

	@Override
	public BdfReader createReader(InputStream in, int nestedLimit,
			int maxBufferSize, boolean canonical) {
		return new BdfReaderImpl(in, nestedLimit, maxBufferSize, canonical);
	}
}




