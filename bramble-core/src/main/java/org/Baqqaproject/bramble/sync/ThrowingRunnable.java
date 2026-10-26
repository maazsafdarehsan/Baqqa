package org.Baqqaproject.bramble.sync;

interface ThrowingRunnable<T extends Throwable> {

	void run() throws T;
}



