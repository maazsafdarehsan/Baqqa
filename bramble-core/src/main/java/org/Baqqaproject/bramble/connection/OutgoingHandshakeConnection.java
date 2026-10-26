package org.Baqqaproject.bramble.connection;

import org.Baqqaproject.bramble.api.connection.ConnectionManager;
import org.Baqqaproject.bramble.api.connection.ConnectionRegistry;
import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.contact.ContactExchangeManager;
import org.Baqqaproject.bramble.api.contact.HandshakeManager;
import org.Baqqaproject.bramble.api.contact.HandshakeManager.HandshakeResult;
import org.Baqqaproject.bramble.api.contact.PendingContactId;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.plugin.TransportId;
import org.Baqqaproject.bramble.api.plugin.duplex.DuplexTransportConnection;
import org.Baqqaproject.bramble.api.transport.KeyManager;
import org.Baqqaproject.bramble.api.transport.StreamContext;
import org.Baqqaproject.bramble.api.transport.StreamReaderFactory;
import org.Baqqaproject.bramble.api.transport.StreamWriter;
import org.Baqqaproject.bramble.api.transport.StreamWriterFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.IOException;
import java.io.InputStream;

import static java.util.logging.Level.WARNING;
import static org.Baqqaproject.bramble.util.LogUtils.logException;

@NotNullByDefault
class OutgoingHandshakeConnection extends HandshakeConnection
		implements Runnable {

	OutgoingHandshakeConnection(KeyManager keyManager,
			ConnectionRegistry connectionRegistry,
			StreamReaderFactory streamReaderFactory,
			StreamWriterFactory streamWriterFactory,
			HandshakeManager handshakeManager,
			ContactExchangeManager contactExchangeManager,
			ConnectionManager connectionManager,
			PendingContactId pendingContactId,
			TransportId transportId, DuplexTransportConnection connection) {
		super(keyManager, connectionRegistry, streamReaderFactory,
				streamWriterFactory, handshakeManager, contactExchangeManager,
				connectionManager, pendingContactId, transportId, connection);
	}

	@Override
	public void run() {
		// Allocate the outgoing stream context
		StreamContext ctxOut =
				allocateStreamContext(pendingContactId, transportId);
		if (ctxOut == null) {
			LOG.warning("Could not allocate stream context");
			onError();
			return;
		}
		// Flush the output stream to send the outgoing stream header
		StreamWriter out;
		try {
			out = streamWriterFactory.createStreamWriter(
					writer.getOutputStream(), ctxOut);
			out.getOutputStream().flush();
		} catch (IOException e) {
			logException(LOG, WARNING, e);
			onError();
			return;
		}
		// Read and recognise the tag
		StreamContext ctxIn = recogniseTag(reader, transportId);
		// Unrecognised tags are suspicious in this case
		if (ctxIn == null) {
			LOG.warning("Unrecognised tag for returning stream");
			onError();
			return;
		}
		// Check that the stream comes from the expected pending contact
		PendingContactId inPendingContactId = ctxIn.getPendingContactId();
		if (inPendingContactId == null) {
			LOG.warning("Expected rendezvous tag, got contact tag");
			onError();
			return;
		}
		if (!inPendingContactId.equals(pendingContactId)) {
			LOG.warning("Wrong pending contact ID for returning stream");
			onError();
			return;
		}
		// Close the connection if it's redundant
		if (!connectionRegistry.registerConnection(pendingContactId)) {
			LOG.info("Redundant rendezvous connection");
			onError();
			return;
		}
		// Handshake and exchange contacts
		try {
			InputStream in = streamReaderFactory.createStreamReader(
					reader.getInputStream(), ctxIn);
			HandshakeResult result =
					handshakeManager.handshake(pendingContactId, in, out);
			Contact contact = contactExchangeManager.exchangeContacts(
					pendingContactId, connection, result.getMasterKey(),
					result.isAlice(), false);
			connectionRegistry.unregisterConnection(pendingContactId, true);
			// Reuse the connection as a transport connection
			connectionManager.manageOutgoingConnection(contact.getId(),
					transportId, connection);
		} catch (IOException | DbException e) {
			logException(LOG, WARNING, e);
			onError();
			connectionRegistry.unregisterConnection(pendingContactId, false);
		}
	}

	private void onError() {
		// 'Recognised' is always true for outgoing connections
		onError(true);
	}
}




