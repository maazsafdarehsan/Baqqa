package org.Baqqaproject.Baqqa.android.reporting;

import org.Baqqaproject.Baqqa.android.viewmodel.ViewModelKey;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class DevReportModule {

	@Binds
	@IntoMap
	@ViewModelKey(ReportViewModel.class)
	abstract ViewModel bindReportViewModel(ReportViewModel reportViewModel);

	@Binds
	abstract Thread.UncaughtExceptionHandler bindUncaughtExceptionHandler(
			BaqqaExceptionHandler handler);

}



