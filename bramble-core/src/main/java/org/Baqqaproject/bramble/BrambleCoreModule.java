package org.Baqqaproject.bramble;

import org.Baqqaproject.bramble.cleanup.CleanupModule;
import org.Baqqaproject.bramble.client.ClientModule;
import org.Baqqaproject.bramble.connection.ConnectionModule;
import org.Baqqaproject.bramble.contact.ContactModule;
import org.Baqqaproject.bramble.crypto.CryptoExecutorModule;
import org.Baqqaproject.bramble.crypto.CryptoModule;
import org.Baqqaproject.bramble.data.DataModule;
import org.Baqqaproject.bramble.db.DatabaseExecutorModule;
import org.Baqqaproject.bramble.db.DatabaseModule;
import org.Baqqaproject.bramble.event.EventModule;
import org.Baqqaproject.bramble.identity.IdentityModule;
import org.Baqqaproject.bramble.io.IoModule;
import org.Baqqaproject.bramble.keyagreement.KeyAgreementModule;
import org.Baqqaproject.bramble.lifecycle.LifecycleModule;
import org.Baqqaproject.bramble.mailbox.MailboxModule;
import org.Baqqaproject.bramble.plugin.PluginModule;
import org.Baqqaproject.bramble.properties.PropertiesModule;
import org.Baqqaproject.bramble.qrcode.QrCodeModule;
import org.Baqqaproject.bramble.record.RecordModule;
import org.Baqqaproject.bramble.reliability.ReliabilityModule;
import org.Baqqaproject.bramble.rendezvous.RendezvousModule;
import org.Baqqaproject.bramble.settings.SettingsModule;
import org.Baqqaproject.bramble.sync.SyncModule;
import org.Baqqaproject.bramble.sync.validation.ValidationModule;
import org.Baqqaproject.bramble.transport.TransportModule;
import org.Baqqaproject.bramble.transport.agreement.TransportKeyAgreementModule;
import org.Baqqaproject.bramble.versioning.VersioningModule;

import dagger.Module;

@Module(includes = {
		CleanupModule.class,
		ClientModule.class,
		ConnectionModule.class,
		ContactModule.class,
		CryptoModule.class,
		CryptoExecutorModule.class,
		DataModule.class,
		DatabaseModule.class,
		DatabaseExecutorModule.class,
		EventModule.class,
		IdentityModule.class,
		IoModule.class,
		KeyAgreementModule.class,
		LifecycleModule.class,
		MailboxModule.class,
		PluginModule.class,
		PropertiesModule.class,
		QrCodeModule.class,
		RecordModule.class,
		ReliabilityModule.class,
		RendezvousModule.class,
		SettingsModule.class,
		SyncModule.class,
		TransportKeyAgreementModule.class,
		TransportModule.class,
		ValidationModule.class,
		VersioningModule.class
})
public class BrambleCoreModule {
}



