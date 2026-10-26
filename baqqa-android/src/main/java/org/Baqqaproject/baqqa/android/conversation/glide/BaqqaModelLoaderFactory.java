package org.Baqqaproject.Baqqa.android.conversation.glide;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import org.Baqqaproject.Baqqa.android.BaqqaApplication;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.InputStream;

@NotNullByDefault
class BaqqaModelLoaderFactory
		implements ModelLoaderFactory<AttachmentHeader, InputStream> {

	private final BaqqaApplication app;

	BaqqaModelLoaderFactory(BaqqaApplication app) {
		this.app = app;
	}

	@Override
	public ModelLoader<AttachmentHeader, InputStream> build(
			MultiModelLoaderFactory multiFactory) {
		return new BaqqaModelLoader(app);
	}

	@Override
	public void teardown() {
		// noop
	}

}




