package org.Baqqaproject.Baqqa.android.controller.handler;

public interface ResultExceptionHandler<R, E extends Exception>
		extends ExceptionHandler<E> {

	void onResult(R result);

}



