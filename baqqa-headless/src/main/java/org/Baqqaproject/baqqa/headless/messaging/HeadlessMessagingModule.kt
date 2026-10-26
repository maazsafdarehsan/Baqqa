package org.Baqqaproject.Baqqa.headless.messaging

import dagger.Module
import dagger.Provides
import org.Baqqaproject.bramble.api.event.EventBus
import javax.inject.Singleton

@Module
class HeadlessMessagingModule {

    @Provides
    @Singleton
    internal fun provideMessagingController(
        eventBus: EventBus, messagingController: MessagingControllerImpl
    ): MessagingController {
        eventBus.addListener(messagingController)
        return messagingController
    }

}



