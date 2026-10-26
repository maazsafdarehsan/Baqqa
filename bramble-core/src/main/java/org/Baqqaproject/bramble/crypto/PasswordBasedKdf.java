package org.Baqqaproject.bramble.crypto;

import org.Baqqaproject.bramble.api.crypto.SecretKey;

interface PasswordBasedKdf {

	int chooseCostParameter();

	SecretKey deriveKey(String password, byte[] salt, int cost);
}



