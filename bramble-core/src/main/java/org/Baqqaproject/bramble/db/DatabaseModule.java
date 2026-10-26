package org.Baqqaproject.bramble.db;

import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.db.DatabaseConfig;
import org.Baqqaproject.bramble.api.db.TransactionManager;
import org.Baqqaproject.bramble.api.event.EventBus;
import org.Baqqaproject.bramble.api.event.EventExecutor;
import org.Baqqaproject.bramble.api.lifecycle.ShutdownManager;
import org.Baqqaproject.bramble.api.sync.MessageFactory;
import org.Baqqaproject.bramble.api.system.Clock;

import java.sql.Connection;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

	@Provides
	@Singleton
	Database<Connection> provideDatabase(DatabaseConfig config,
			MessageFactory messageFactory, Clock clock) {
		return new H2Database(config, messageFactory, clock);
	}

	@Provides
	@Singleton
	DatabaseComponent provideDatabaseComponent(Database<Connection> db,
			EventBus eventBus, @EventExecutor Executor eventExecutor,
			ShutdownManager shutdownManager) {
		return new DatabaseComponentImpl<>(db, Connection.class, eventBus,
				eventExecutor, shutdownManager);
	}

	@Provides
	TransactionManager provideTransactionManager(DatabaseComponent db) {
		return db;
	}
}



