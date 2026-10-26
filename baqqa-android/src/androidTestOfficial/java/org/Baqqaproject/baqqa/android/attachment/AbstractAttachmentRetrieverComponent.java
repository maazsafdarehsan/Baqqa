package org.Baqqaproject.Baqqa.android.attachment;

import org.Baqqaproject.Baqqa.android.attachment.media.MediaModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		MediaModule.class
})
interface AbstractAttachmentRetrieverComponent {

	void inject(AttachmentRetrieverIntegrationTest test);

}



