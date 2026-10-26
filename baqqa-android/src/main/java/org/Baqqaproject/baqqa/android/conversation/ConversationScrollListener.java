package org.Baqqaproject.Baqqa.android.conversation;

import org.Baqqaproject.Baqqa.android.view.BaqqaRecyclerViewScrollListener;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
class ConversationScrollListener extends
		BaqqaRecyclerViewScrollListener<ConversationAdapter, ConversationItem> {

	private final ConversationViewModel viewModel;

	protected ConversationScrollListener(ConversationAdapter adapter,
			ConversationViewModel viewModel) {
		super(adapter);
		this.viewModel = viewModel;
	}

	@Override
	protected void onItemVisible(ConversationItem item) {
		if (!item.isRead()) {
			viewModel.markMessageRead(item.getGroupId(), item.getId());
			item.markRead();
		}
	}

}




