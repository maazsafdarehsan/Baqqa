package org.Baqqaproject.bramble.db;

import org.Baqqaproject.bramble.api.db.DatabaseConfig;
import org.Baqqaproject.bramble.api.sync.MessageFactory;
import org.Baqqaproject.bramble.api.system.Clock;
import org.junit.Ignore;

import java.sql.Connection;

/**
 * Sanity check for {@link DatabasePerformanceComparisonTest}: check that
 * if conditions A and B are identical, no significant difference is (usually)
 * detected.
 */
@Ignore
public class H2SelfDatabasePerformanceComparisonTest
		extends DatabasePerformanceComparisonTest {

	@Override
	Database<Connection> createDatabase(boolean conditionA,
			DatabaseConfig databaseConfig, MessageFactory messageFactory,
			Clock clock) {
		return new H2Database(databaseConfig, messageFactory, clock);
	}

	@Override
	protected String getTestName() {
		return getClass().getSimpleName();
	}
}



