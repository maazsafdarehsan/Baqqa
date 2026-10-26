package org.Baqqaproject.Baqqa.android.removabledrive;

import org.Baqqaproject.Baqqa.android.viewmodel.ViewModelKey;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface TransferDataModule {

	@Binds
	@IntoMap
	@ViewModelKey(RemovableDriveViewModel.class)
	ViewModel bindRemovableDriveViewModel(
			RemovableDriveViewModel removableDriveViewModel);

}



