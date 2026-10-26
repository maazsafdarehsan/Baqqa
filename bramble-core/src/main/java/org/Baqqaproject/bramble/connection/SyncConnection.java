package org.Baqqaproject.bramble.connection;

import org.Baqqaproject.bramble.api.connection.ConnectionRegistry;
import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.plugin.TransportConnectionReader;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.Baqqaproject.bramble.api.properties.TransportPropertyManager;
import org.Baqqaproject.bramble.api.sync.PriorityHandler;
import org.Baqqaproject.bramble.api.sync.SyncSession;
import org.Baqqaproject.bramble.api.sync.SyncSessionFactory;
import org.Baqqaproject.bramble.api.transport.KeyManager;
import org.Baqqaproject.bramble.api.transport.StreamContext;
import org.Baqqaproject.bramble.api.transport.StreamReaderFactory;
import org.Baqqaproject.bramble.api.transport.StreamWriterFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import static java.util.logging.Level.WARNING;
import static org.Baqqaproject.bramble.util.LogUtils.logException;
import static org.briarproject.nullsafety.NullSafety.requireNonNull;

@NotNullByDefault
class SyncConnection extends Connection {

	final SyncSessionFactory syncSessionFactory;
	final TransportPropertyManager transportPropertyManager;

	SyncConnection(KeyManager keyManager, ConnectionRegistry connectionRegistry,
			StreamReaderFactory streamReaderFactory,
			StreamWriterFactory streamWriterFactory,
			SyncSessionFactory syncSessionFactory,
			TransportPropertyManager transportPropertyManager) {
		super(keyManager, connectionRegistry, streamReaderFactory,
				streamWriterFactory);
		this.syncSessionFactory = syncSessionFactory;
		this.transportPropertyManager = transportPropertyManager;
	}

	@Nullable
	StreamContext allocateStreamContext(ContactId contactId,
			TransportId transportId) {
		try {
			return keyManager.getStreamContext(contactId, transportId);
		} catch (DbException e) {
			logException(LOG, WARNING, e);
			return null;
		}
	}

	SyncSession createIncomingSession(StreamContext ctx,
			TransportConnectionReader r, PriorityHandler handler)
			throws IOException {
		InputStream streamReader = streamReaderFactory.createStreamReader(
				r.getInputStream(), ctx);
		ContactId c = requireNonNull(ctx.getContactId());
		return syncSessionFactory
				.createIncomingSession(c, streamReader, handler);
	}
}




