package org.Baqqaproject.bramble.api.sync.validation;

import org.Baqqaproject.bramble.api.crypto.SecretKey;
import org.Baqqaproject.bramble.api.lifecycle.LifecycleManager;
import org.Baqqaproject.bramble.api.sync.ClientId;
import org.briarproject.nullsafety.NotNullByDefault;

/**
 * Responsible for managing message validators and passing them messages to
 * validate.
 */
@NotNullByDefault
public interface ValidationManager {

	/**
	 * Registers the {@link MessageValidator} for the given client. This method
	 * should be called before
	 * {@link LifecycleManager#startServices(SecretKey)}.
	 */
	void registerMessageValidator(ClientId c, int majorVersion,
			MessageValidator v);

	/**
	 * Registers the {@link IncomingMessageHook} for the given client. The hook
	 * will be called once for each incoming message that passes validation.
	 * This method should be called before
	 * {@link LifecycleManager#startServices(SecretKey)}.
	 */
	void registerIncomingMessageHook(ClientId c, int majorVersion,
			IncomingMessageHook hook);
}




