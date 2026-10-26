package org.Baqqaproject.Baqqa.forum;

import org.Baqqaproject.bramble.api.FeatureFlags;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.data.MetadataEncoder;
import org.Baqqaproject.bramble.api.sync.validation.ValidationManager;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.Baqqa.api.forum.ForumFactory;
import org.Baqqaproject.Baqqa.api.forum.ForumManager;
import org.Baqqaproject.Baqqa.api.forum.ForumPostFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.Baqqaproject.Baqqa.api.forum.ForumManager.CLIENT_ID;
import static org.Baqqaproject.Baqqa.api.forum.ForumManager.MAJOR_VERSION;

@Module
public class ForumModule {

	public static class EagerSingletons {
		@Inject
		ForumManager forumManager;
		@Inject
		ForumPostValidator forumPostValidator;
	}

	@Provides
	@Singleton
	ForumManager provideForumManager(ForumManagerImpl forumManager,
			ValidationManager validationManager,
			FeatureFlags featureFlags) {
		if (!featureFlags.shouldEnableForumsInCore()) {
			return forumManager;
		}
		validationManager.registerIncomingMessageHook(CLIENT_ID, MAJOR_VERSION,
				forumManager);
		return forumManager;
	}

	@Provides
	ForumPostFactory provideForumPostFactory(
			ForumPostFactoryImpl forumPostFactory) {
		return forumPostFactory;
	}

	@Provides
	ForumFactory provideForumFactory(ForumFactoryImpl forumFactory) {
		return forumFactory;
	}

	@Provides
	@Singleton
	ForumPostValidator provideForumPostValidator(
			ValidationManager validationManager, ClientHelper clientHelper,
			MetadataEncoder metadataEncoder, Clock clock,
			FeatureFlags featureFlags) {
		ForumPostValidator validator = new ForumPostValidator(clientHelper,
				metadataEncoder, clock);
		if (featureFlags.shouldEnableForumsInCore()) {
			validationManager.registerMessageValidator(CLIENT_ID, MAJOR_VERSION,
					validator);
		}
		return validator;
	}

}



