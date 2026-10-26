package org.Baqqaproject.Baqqa.android.contact;

import org.Baqqaproject.Baqqa.android.viewmodel.ViewModelKey;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ContactListModule {

	@Binds
	@IntoMap
	@ViewModelKey(ContactListViewModel.class)
	abstract ViewModel bindContactListViewModel(
			ContactListViewModel contactListViewModel);

}



