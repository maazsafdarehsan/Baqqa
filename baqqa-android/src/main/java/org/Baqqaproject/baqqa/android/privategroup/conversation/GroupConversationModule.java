package org.Baqqaproject.Baqqa.android.privategroup.conversation;

import org.Baqqaproject.Baqqa.android.viewmodel.ViewModelKey;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface GroupConversationModule {

	@Binds
	@IntoMap
	@ViewModelKey(GroupViewModel.class)
	ViewModel bindGroupViewModel(GroupViewModel groupViewModel);

}



