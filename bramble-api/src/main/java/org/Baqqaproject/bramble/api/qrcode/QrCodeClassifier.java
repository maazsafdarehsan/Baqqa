package org.Baqqaproject.bramble.api.qrcode;

import org.Baqqaproject.bramble.api.Pair;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface QrCodeClassifier {

	enum QrCodeType {
		BQP,
		MAILBOX,
		UNKNOWN
	}

	Pair<QrCodeType, Integer> classifyQrCode(String payload);
}




