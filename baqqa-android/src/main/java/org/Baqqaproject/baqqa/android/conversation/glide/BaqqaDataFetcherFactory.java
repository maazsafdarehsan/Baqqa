package org.Baqqaproject.Baqqa.android.conversation.glide;

import org.Baqqaproject.bramble.api.db.DatabaseExecutor;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentReader;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.concurrent.Executor;

import javax.inject.Inject;

@NotNullByDefault
public class BaqqaDataFetcherFactory {

	private final AttachmentReader attachmentReader;
	@DatabaseExecutor
	private final Executor dbExecutor;

	@Inject
	public BaqqaDataFetcherFactory(AttachmentReader attachmentReader,
			@DatabaseExecutor Executor dbExecutor) {
		this.attachmentReader = attachmentReader;
		this.dbExecutor = dbExecutor;
	}

	BaqqaDataFetcher createBaqqaDataFetcher(AttachmentHeader model) {
		return new BaqqaDataFetcher(attachmentReader, dbExecutor, model);
	}

}




