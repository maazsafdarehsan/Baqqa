package org.Baqqaproject.bramble.transport.agreement;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.crypto.KeyPair;
import org.Baqqaproject.bramble.api.crypto.PrivateKey;
import org.Baqqaproject.bramble.api.crypto.PublicKey;
import org.Baqqaproject.bramble.api.crypto.SecretKey;
import org.briarproject.nullsafety.NotNullByDefault;

import java.security.GeneralSecurityException;

@NotNullByDefault
interface TransportKeyAgreementCrypto {

	KeyPair generateKeyPair();

	SecretKey deriveRootKey(KeyPair localKeyPair, PublicKey remotePublicKey)
			throws GeneralSecurityException;

	PublicKey parsePublicKey(byte[] encoded) throws FormatException;

	PrivateKey parsePrivateKey(byte[] encoded) throws FormatException;
}




