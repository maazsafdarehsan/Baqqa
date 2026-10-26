package org.Baqqaproject.Baqqa.android.blog;

import org.Baqqaproject.Baqqa.android.viewmodel.ViewModelKey;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface BlogModule {

	@Binds
	@IntoMap
	@ViewModelKey(FeedViewModel.class)
	ViewModel bindFeedViewModel(FeedViewModel feedViewModel);

	@Binds
	@IntoMap
	@ViewModelKey(BlogViewModel.class)
	ViewModel bindBlogViewModel(BlogViewModel blogViewModel);

	@Binds
	@IntoMap
	@ViewModelKey(RssFeedViewModel.class)
	ViewModel bindRssFeedViewModel(RssFeedViewModel rssFeedViewModel);
}



