package org.Baqqaproject.Baqqa.android;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

public class BaqqaTestRunner extends AndroidJUnitRunner {

	@Override
	public Application newApplication(ClassLoader cl, String className,
			Context context)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return super.newApplication(cl,
				BaqqaTestComponentApplication.class.getName(), context);
	}

}



