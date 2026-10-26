package org.Baqqaproject.Baqqa.blog;

import org.Baqqaproject.bramble.api.FeatureFlags;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.contact.ContactManager;
import org.Baqqaproject.bramble.api.data.MetadataEncoder;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.sync.GroupFactory;
import org.Baqqaproject.bramble.api.sync.MessageFactory;
import org.Baqqaproject.bramble.api.sync.validation.ValidationManager;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.Baqqa.api.blog.BlogFactory;
import org.Baqqaproject.Baqqa.api.blog.BlogManager;
import org.Baqqaproject.Baqqa.api.blog.BlogPostFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.Baqqaproject.Baqqa.api.blog.BlogManager.CLIENT_ID;
import static org.Baqqaproject.Baqqa.api.blog.BlogManager.MAJOR_VERSION;

@Module
public class BlogModule {

	public static class EagerSingletons {
		@Inject
		BlogPostValidator blogPostValidator;
		@Inject
		BlogManager blogManager;
	}

	@Provides
	@Singleton
	BlogManager provideBlogManager(BlogManagerImpl blogManager,
			LifecycleManager lifecycleManager, ContactManager contactManager,
			ValidationManager validationManager, FeatureFlags featureFlags) {
		if (!featureFlags.shouldEnableBlogsInCore()) {
			return blogManager;
		}
		lifecycleManager.registerOpenDatabaseHook(blogManager);
		contactManager.registerContactHook(blogManager);
		validationManager.registerIncomingMessageHook(CLIENT_ID, MAJOR_VERSION,
				blogManager);
		return blogManager;
	}

	@Provides
	BlogPostFactory provideBlogPostFactory(
			BlogPostFactoryImpl blogPostFactory) {
		return blogPostFactory;
	}

	@Provides
	BlogFactory provideBlogFactory(BlogFactoryImpl blogFactory) {
		return blogFactory;
	}

	@Provides
	@Singleton
	BlogPostValidator provideBlogPostValidator(
			ValidationManager validationManager, GroupFactory groupFactory,
			MessageFactory messageFactory, BlogFactory blogFactory,
			ClientHelper clientHelper, MetadataEncoder metadataEncoder,
			Clock clock, FeatureFlags featureFlags) {
		BlogPostValidator validator = new BlogPostValidator(groupFactory,
				messageFactory, blogFactory, clientHelper, metadataEncoder,
				clock);
		if (featureFlags.shouldEnableBlogsInCore()) {
			validationManager.registerMessageValidator(CLIENT_ID, MAJOR_VERSION,
					validator);
		}
		return validator;
	}

}



