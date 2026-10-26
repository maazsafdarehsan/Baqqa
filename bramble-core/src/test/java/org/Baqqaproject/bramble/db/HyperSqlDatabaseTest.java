package org.Baqqaproject.bramble.db;

import org.Baqqaproject.bramble.api.db.DatabaseConfig;
import org.Baqqaproject.bramble.api.sync.MessageFactory;
import org.Baqqaproject.bramble.api.system.Clock;
import org.junit.Before;

import static org.Baqqaproject.bramble.test.TestUtils.isCryptoStrengthUnlimited;
import static org.junit.Assume.assumeTrue;

public class HyperSqlDatabaseTest extends JdbcDatabaseTest {

	@Before
	public void setUp() {
		assumeTrue(isCryptoStrengthUnlimited());
	}

	@Override
	protected JdbcDatabase createDatabase(DatabaseConfig config,
			MessageFactory messageFactory, Clock clock) {
		return new HyperSqlDatabase(config, messageFactory ,clock);
	}
}



