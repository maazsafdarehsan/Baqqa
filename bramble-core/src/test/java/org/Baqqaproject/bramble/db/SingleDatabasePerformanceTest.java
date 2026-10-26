package org.Baqqaproject.bramble.db;

import org.Baqqaproject.bramble.api.crypto.SecretKey;
import org.Baqqaproject.bramble.api.db.DatabaseConfig;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.sync.MessageFactory;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.bramble.system.SystemClock;
import org.Baqqaproject.bramble.test.TestDatabaseConfig;
import org.Baqqaproject.bramble.test.TestMessageFactory;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import static org.Baqqaproject.bramble.test.TestUtils.deleteTestDirectory;
import static org.Baqqaproject.bramble.test.TestUtils.getMean;
import static org.Baqqaproject.bramble.test.TestUtils.getMedian;
import static org.Baqqaproject.bramble.test.TestUtils.getSecretKey;
import static org.Baqqaproject.bramble.test.TestUtils.getStandardDeviation;

public abstract class SingleDatabasePerformanceTest
		extends DatabasePerformanceTest {

	abstract Database<Connection> createDatabase(DatabaseConfig databaseConfig,
			MessageFactory messageFactory, Clock clock);

	private SecretKey databaseKey = getSecretKey();

	@Override
	protected void benchmark(String name,
			BenchmarkTask<Database<Connection>> task) throws Exception {
		deleteTestDirectory(testDir);
		Database<Connection> db = openDatabase();
		populateDatabase(db);
		db.close();
		db = openDatabase();
		// Measure the first iteration
		long firstDuration = measureOne(db, task);
		// Measure blocks of iterations until we reach a steady state
		SteadyStateResult result = measureSteadyState(db, task);
		db.close();
		writeResult(name, result.blocks, firstDuration, result.durations);
	}

	private Database<Connection> openDatabase() throws DbException {
		Database<Connection> db = createDatabase(
				new TestDatabaseConfig(testDir), new TestMessageFactory(),
				new SystemClock());
		db.open(databaseKey, null);
		return db;
	}

	private void writeResult(String name, int blocks, long firstDuration,
			List<Double> durations) throws IOException {
		String result = String.format("%s\t%d\t%,d\t%,d\t%,d\t%,d", name,
				blocks, firstDuration, (long) getMean(durations),
				(long) getMedian(durations),
				(long) getStandardDeviation(durations));
		writeResult(result);
	}
}



