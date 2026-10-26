package org.Baqqaproject.bramble.rendezvous;

import org.Baqqaproject.bramble.api.crypto.CryptoComponent;
import org.Baqqaproject.bramble.api.crypto.SecretKey;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.Baqqaproject.bramble.api.rendezvous.KeyMaterialSource;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.Baqqaproject.bramble.rendezvous.RendezvousConstants.KEY_MATERIAL_LABEL;
import static org.Baqqaproject.bramble.rendezvous.RendezvousConstants.PROTOCOL_VERSION;
import static org.Baqqaproject.bramble.rendezvous.RendezvousConstants.RENDEZVOUS_KEY_LABEL;
import static org.Baqqaproject.bramble.util.StringUtils.toUtf8;

@Immutable
@NotNullByDefault
class RendezvousCryptoImpl implements RendezvousCrypto {

	private final CryptoComponent crypto;

	@Inject
	RendezvousCryptoImpl(CryptoComponent crypto) {
		this.crypto = crypto;
	}

	@Override
	public SecretKey deriveRendezvousKey(SecretKey staticMasterKey) {
		return crypto.deriveKey(RENDEZVOUS_KEY_LABEL, staticMasterKey,
				new byte[] {PROTOCOL_VERSION});
	}

	@Override
	public KeyMaterialSource createKeyMaterialSource(SecretKey rendezvousKey,
			TransportId t) {
		SecretKey sourceKey = crypto.deriveKey(KEY_MATERIAL_LABEL,
				rendezvousKey, toUtf8(t.getString()));
		return new KeyMaterialSourceImpl(sourceKey);
	}
}




