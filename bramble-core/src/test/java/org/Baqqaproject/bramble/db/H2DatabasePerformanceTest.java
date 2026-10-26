package org.Baqqaproject.bramble.db;

import org.Baqqaproject.bramble.api.db.DatabaseConfig;
import org.Baqqaproject.bramble.api.sync.MessageFactory;
import org.Baqqaproject.bramble.api.system.Clock;
import org.junit.Ignore;

@Ignore
public class H2DatabasePerformanceTest extends SingleDatabasePerformanceTest {

	@Override
	protected String getTestName() {
		return getClass().getSimpleName();
	}

	@Override
	protected JdbcDatabase createDatabase(DatabaseConfig config,
			MessageFactory messageFactory, Clock clock) {
		return new H2Database(config, messageFactory, clock);
	}
}



