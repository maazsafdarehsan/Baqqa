package org.Baqqaproject.Baqqa.android.conversation;

import org.Baqqaproject.Baqqa.android.activity.ActivityScope;
import org.Baqqaproject.Baqqa.android.conversation.glide.BaqqaDataFetcherFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ConversationModule {

	@ActivityScope
	@Provides
	BaqqaDataFetcherFactory provideBaqqaDataFetcherFactory(
			BaqqaDataFetcherFactory dataFetcherFactory) {
		return dataFetcherFactory;
	}

}



