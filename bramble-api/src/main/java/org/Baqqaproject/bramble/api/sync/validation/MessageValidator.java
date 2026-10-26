package org.Baqqaproject.bramble.api.sync.validation;

import org.Baqqaproject.bramble.api.sync.Group;
import org.Baqqaproject.bramble.api.sync.InvalidMessageException;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageContext;

public interface MessageValidator {

	/**
	 * Validates the given message and returns its metadata and
	 * dependencies.
	 */
	MessageContext validateMessage(Message m, Group g)
			throws InvalidMessageException;
}



