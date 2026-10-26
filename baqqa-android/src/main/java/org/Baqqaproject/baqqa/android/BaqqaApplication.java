package org.Baqqaproject.Baqqa.android;

import android.app.Activity;
import android.content.SharedPreferences;

import org.Baqqaproject.bramble.BrambleApplication;
import org.Baqqaproject.Baqqa.android.navdrawer.NavDrawerActivity;

/**
 * This exists so that the Application object will not necessarily be cast
 * directly to the Baqqa application object.
 */
public interface BaqqaApplication extends BrambleApplication {

	Class<? extends Activity> ENTRY_ACTIVITY = NavDrawerActivity.class;

	AndroidComponent getApplicationComponent();

	SharedPreferences getDefaultSharedPreferences();

	boolean isRunningInBackground();

	boolean isInstrumentationTest();
}



