package org.Baqqaproject.Baqqa.privategroup;

import org.Baqqaproject.bramble.api.FeatureFlags;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.data.MetadataEncoder;
import org.Baqqaproject.bramble.api.sync.validation.ValidationManager;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.Baqqa.api.privategroup.GroupMessageFactory;
import org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupFactory;
import org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupManager;
import org.Baqqaproject.Baqqa.api.privategroup.invitation.GroupInvitationFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupManager.CLIENT_ID;
import static org.Baqqaproject.Baqqa.api.privategroup.PrivateGroupManager.MAJOR_VERSION;

@Module
public class PrivateGroupModule {

	public static class EagerSingletons {
		@Inject
		GroupMessageValidator groupMessageValidator;
		@Inject
		PrivateGroupManager groupManager;
	}

	@Provides
	@Singleton
	PrivateGroupManager provideGroupManager(
			PrivateGroupManagerImpl groupManager,
			ValidationManager validationManager,
			FeatureFlags featureFlags) {
		if (!featureFlags.shouldEnablePrivateGroupsInCore()) {
			return groupManager;
		}
		validationManager.registerIncomingMessageHook(CLIENT_ID, MAJOR_VERSION,
				groupManager);
		return groupManager;
	}

	@Provides
	PrivateGroupFactory providePrivateGroupFactory(
			PrivateGroupFactoryImpl privateGroupFactory) {
		return privateGroupFactory;
	}

	@Provides
	GroupMessageFactory provideGroupMessageFactory(
			GroupMessageFactoryImpl groupMessageFactory) {
		return groupMessageFactory;
	}

	@Provides
	@Singleton
	GroupMessageValidator provideGroupMessageValidator(
			PrivateGroupFactory privateGroupFactory,
			ClientHelper clientHelper, MetadataEncoder metadataEncoder,
			Clock clock, GroupInvitationFactory groupInvitationFactory,
			ValidationManager validationManager, FeatureFlags featureFlags) {
		GroupMessageValidator validator = new GroupMessageValidator(
				privateGroupFactory, clientHelper, metadataEncoder, clock,
				groupInvitationFactory);
		if (featureFlags.shouldEnablePrivateGroupsInCore()) {
			validationManager.registerMessageValidator(CLIENT_ID, MAJOR_VERSION,
					validator);
		}
		return validator;
	}

}



