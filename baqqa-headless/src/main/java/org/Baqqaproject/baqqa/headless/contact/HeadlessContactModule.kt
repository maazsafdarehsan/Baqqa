package org.Baqqaproject.Baqqa.headless.contact

import dagger.Module
import dagger.Provides
import org.Baqqaproject.bramble.api.event.EventBus
import javax.inject.Singleton

@Module
class HeadlessContactModule {

    @Provides
    @Singleton
    internal fun provideContactController(
        eventBus: EventBus,
        contactController: ContactControllerImpl
    ): ContactController {
        eventBus.addListener(contactController)
        return contactController
    }

}



