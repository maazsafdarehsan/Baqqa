package org.Baqqaproject.Baqqa.android.conversation.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.Baqqaproject.Baqqa.android.BaqqaApplication;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.InputStream;

import static android.util.Log.DEBUG;
import static android.util.Log.WARN;
import static org.Baqqaproject.Baqqa.android.TestingConstants.IS_DEBUG_BUILD;

@GlideModule
@NotNullByDefault
public final class BaqqaGlideModule extends AppGlideModule {

	@Override
	public void registerComponents(Context context, Glide glide,
			Registry registry) {
		BaqqaApplication app =
				(BaqqaApplication) context.getApplicationContext();
		BaqqaModelLoaderFactory factory = new BaqqaModelLoaderFactory(app);
		registry.prepend(AttachmentHeader.class, InputStream.class, factory);
	}

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		builder.setLogLevel(IS_DEBUG_BUILD ? DEBUG : WARN);
	}

	@Override
	public boolean isManifestParsingEnabled() {
		return false;
	}

}



