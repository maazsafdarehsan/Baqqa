package org.Baqqaproject.Baqqa.android.conversation.glide;


import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import org.Baqqaproject.Baqqa.android.BaqqaApplication;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.briarproject.nullsafety.MethodsNotNullByDefault;
import org.briarproject.nullsafety.ParametersNotNullByDefault;

import java.io.InputStream;

import javax.inject.Inject;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public final class BaqqaModelLoader
		implements ModelLoader<AttachmentHeader, InputStream> {

	@Inject
	BaqqaDataFetcherFactory dataFetcherFactory;

	BaqqaModelLoader(BaqqaApplication app) {
		app.getApplicationComponent().inject(this);
	}

	@Override
	public LoadData<InputStream> buildLoadData(AttachmentHeader model,
			int width, int height, Options options) {
		ObjectKey key = new ObjectKey(model.getMessageId());
		BaqqaDataFetcher dataFetcher =
				dataFetcherFactory.createBaqqaDataFetcher(model);
		return new LoadData<>(key, dataFetcher);
	}

	@Override
	public boolean handles(AttachmentHeader model) {
		return true;
	}
}




