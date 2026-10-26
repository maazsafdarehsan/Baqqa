package org.Baqqaproject.bramble.api.crypto;

import org.Baqqaproject.bramble.api.Bytes;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

import static org.Baqqaproject.bramble.api.crypto.CryptoConstants.KEY_TYPE_SIGNATURE;

/**
 * Type-safe wrapper for a public key used for signing.
 */
@Immutable
@NotNullByDefault
public class SignaturePrivateKey extends Bytes implements PrivateKey {

	public SignaturePrivateKey(byte[] bytes) {
		super(bytes);
	}

	@Override
	public String getKeyType() {
		return KEY_TYPE_SIGNATURE;
	}

	@Override
	public byte[] getEncoded() {
		return getBytes();
	}
}




