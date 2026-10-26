package org.Baqqaproject.bramble.rendezvous;

import org.Baqqaproject.bramble.api.crypto.SecretKey;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.Baqqaproject.bramble.api.rendezvous.KeyMaterialSource;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface RendezvousCrypto {

	SecretKey deriveRendezvousKey(SecretKey staticMasterKey);

	KeyMaterialSource createKeyMaterialSource(SecretKey rendezvousKey,
			TransportId t);
}




