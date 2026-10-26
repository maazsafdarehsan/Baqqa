package org.Baqqaproject.Baqqa.api.introduction.event;

import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.Baqqa.api.conversation.event.ConversationMessageReceivedEvent;
import org.Baqqaproject.Baqqa.api.introduction.IntroductionResponse;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class IntroductionResponseReceivedEvent extends
		ConversationMessageReceivedEvent<IntroductionResponse> {

	public IntroductionResponseReceivedEvent(
			IntroductionResponse introductionResponse, ContactId contactId) {
		super(introductionResponse, contactId);
	}

}




