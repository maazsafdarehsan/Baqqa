package org.Baqqaproject.Baqqa.android.mailbox;

import org.Baqqaproject.Baqqa.android.viewmodel.ViewModelKey;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface MailboxModule {

	@Binds
	@IntoMap
	@ViewModelKey(MailboxViewModel.class)
	ViewModel bindMailboxViewModel(MailboxViewModel mailboxViewModel);

}



