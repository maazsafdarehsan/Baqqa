package org.Baqqaproject.Baqqa.avatar;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.bramble.api.data.BdfReader;
import org.Baqqaproject.bramble.api.data.BdfReaderFactory;
import org.Baqqaproject.bramble.api.data.MetadataEncoder;
import org.Baqqaproject.bramble.api.db.Metadata;
import org.Baqqaproject.bramble.api.sync.Group;
import org.Baqqaproject.bramble.api.sync.InvalidMessageException;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageContext;
import org.Baqqaproject.bramble.api.sync.validation.MessageValidator;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.Baqqa.attachment.CountingInputStream;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.concurrent.Immutable;

import static org.Baqqaproject.bramble.api.sync.SyncConstants.MAX_MESSAGE_BODY_LENGTH;
import static org.Baqqaproject.bramble.api.transport.TransportConstants.MAX_CLOCK_DIFFERENCE;
import static org.Baqqaproject.bramble.util.ValidationUtils.checkLength;
import static org.Baqqaproject.bramble.util.ValidationUtils.checkSize;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MAX_CONTENT_TYPE_BYTES;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_CONTENT_TYPE;
import static org.Baqqaproject.Baqqa.api.attachment.MediaConstants.MSG_KEY_DESCRIPTOR_LENGTH;
import static org.Baqqaproject.Baqqa.avatar.AvatarConstants.MSG_KEY_VERSION;
import static org.Baqqaproject.Baqqa.avatar.AvatarConstants.MSG_TYPE_UPDATE;

@Immutable
@NotNullByDefault
class AvatarValidator implements MessageValidator {

	private final BdfReaderFactory bdfReaderFactory;
	private final MetadataEncoder metadataEncoder;
	private final Clock clock;

	AvatarValidator(BdfReaderFactory bdfReaderFactory,
			MetadataEncoder metadataEncoder, Clock clock) {
		this.bdfReaderFactory = bdfReaderFactory;
		this.metadataEncoder = metadataEncoder;
		this.clock = clock;
	}

	@Override
	public MessageContext validateMessage(Message m, Group g)
			throws InvalidMessageException {
		// Reject the message if it's too far in the future
		long now = clock.currentTimeMillis();
		if (m.getTimestamp() - now > MAX_CLOCK_DIFFERENCE) {
			throw new InvalidMessageException(
					"Timestamp is too far in the future");
		}
		try {
			InputStream in = new ByteArrayInputStream(m.getBody());
			CountingInputStream countIn =
					new CountingInputStream(in, MAX_MESSAGE_BODY_LENGTH);
			BdfReader reader = bdfReaderFactory.createReader(countIn);
			BdfList list = reader.readList();
			long bytesRead = countIn.getBytesRead();
			BdfDictionary d = validateUpdate(list, bytesRead);
			Metadata meta = metadataEncoder.encode(d);
			return new MessageContext(meta);
		} catch (IOException e) {
			throw new InvalidMessageException(e);
		}
	}

	private BdfDictionary validateUpdate(BdfList body, long descriptorLength)
			throws FormatException {
		// 0.0: Message Type, Version, Content-Type
		checkSize(body, 3);
		// Message Type
		int messageType = body.getInt(0);
		if (messageType != MSG_TYPE_UPDATE) throw new FormatException();
		// Version
		long version = body.getLong(1);
		if (version < 0) throw new FormatException();
		// Content-Type
		String contentType = body.getString(2);
		checkLength(contentType, 1, MAX_CONTENT_TYPE_BYTES);

		// Return the metadata
		BdfDictionary meta = new BdfDictionary();
		meta.put(MSG_KEY_VERSION, version);
		meta.put(MSG_KEY_CONTENT_TYPE, contentType);
		meta.put(MSG_KEY_DESCRIPTOR_LENGTH, descriptorLength);
		return meta;
	}

}




