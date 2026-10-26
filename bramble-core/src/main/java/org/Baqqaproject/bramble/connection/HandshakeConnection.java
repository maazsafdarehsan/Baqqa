package org.Baqqaproject.bramble.connection;

import org.Baqqaproject.bramble.api.connection.ConnectionManager;
import org.Baqqaproject.bramble.api.connection.ConnectionRegistry;
import org.Baqqaproject.bramble.api.contact.ContactExchangeManager;
import org.Baqqaproject.bramble.api.contact.HandshakeManager;
import org.Baqqaproject.bramble.api.contact.PendingContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.plugin.TransportConnectionReader;
import org.Baqqaproject.bramble.api.plugin.TransportConnectionWriter;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.Baqqaproject.bramble.api.plugin.duplex.DuplexTransportConnection;
import org.Baqqaproject.bramble.api.transport.KeyManager;
import org.Baqqaproject.bramble.api.transport.StreamContext;
import org.Baqqaproject.bramble.api.transport.StreamReaderFactory;
import org.Baqqaproject.bramble.api.transport.StreamWriterFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.Nullable;

import static java.util.logging.Level.WARNING;
import static org.Baqqaproject.bramble.util.LogUtils.logException;

@NotNullByDefault
abstract class HandshakeConnection extends Connection {

	final HandshakeManager handshakeManager;
	final ContactExchangeManager contactExchangeManager;
	final ConnectionManager connectionManager;
	final PendingContactId pendingContactId;
	final TransportId transportId;
	final DuplexTransportConnection connection;
	final TransportConnectionReader reader;
	final TransportConnectionWriter writer;

	HandshakeConnection(KeyManager keyManager,
			ConnectionRegistry connectionRegistry,
			StreamReaderFactory streamReaderFactory,
			StreamWriterFactory streamWriterFactory,
			HandshakeManager handshakeManager,
			ContactExchangeManager contactExchangeManager,
			ConnectionManager connectionManager,
			PendingContactId pendingContactId,
			TransportId transportId, DuplexTransportConnection connection) {
		super(keyManager, connectionRegistry, streamReaderFactory,
				streamWriterFactory);
		this.handshakeManager = handshakeManager;
		this.contactExchangeManager = contactExchangeManager;
		this.connectionManager = connectionManager;
		this.pendingContactId = pendingContactId;
		this.transportId = transportId;
		this.connection = connection;
		reader = connection.getReader();
		writer = connection.getWriter();
	}

	@Nullable
	StreamContext allocateStreamContext(PendingContactId pendingContactId,
			TransportId transportId) {
		try {
			return keyManager.getStreamContext(pendingContactId, transportId);
		} catch (DbException e) {
			logException(LOG, WARNING, e);
			return null;
		}
	}

	void onError(boolean recognised) {
		disposeOnError(reader, recognised);
		disposeOnError(writer);
	}
}




