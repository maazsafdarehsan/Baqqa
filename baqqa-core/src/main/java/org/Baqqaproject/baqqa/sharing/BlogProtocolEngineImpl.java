package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.contact.ContactId;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.event.Event;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.bramble.api.versioning.ClientVersioningManager;
import org.Baqqaproject.Baqqa.api.autodelete.AutoDeleteManager;
import org.Baqqaproject.Baqqa.api.blog.Blog;
import org.Baqqaproject.Baqqa.api.blog.BlogInvitationResponse;
import org.Baqqaproject.Baqqa.api.blog.BlogManager;
import org.Baqqaproject.Baqqa.api.blog.BlogSharingManager;
import org.Baqqaproject.Baqqa.api.blog.event.BlogInvitationRequestReceivedEvent;
import org.Baqqaproject.Baqqa.api.blog.event.BlogInvitationResponseReceivedEvent;
import org.Baqqaproject.Baqqa.api.conversation.ConversationManager;
import org.Baqqaproject.Baqqa.api.conversation.ConversationRequest;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

@Immutable
@NotNullByDefault
class BlogProtocolEngineImpl extends ProtocolEngineImpl<Blog> {

	private final BlogManager blogManager;
	private final InvitationFactory<Blog, BlogInvitationResponse>
			invitationFactory;

	@Inject
	BlogProtocolEngineImpl(
			DatabaseComponent db,
			ClientHelper clientHelper,
			ClientVersioningManager clientVersioningManager,
			MessageEncoder messageEncoder,
			MessageParser<Blog> messageParser,
			AutoDeleteManager autoDeleteManager,
			ConversationManager conversationManager,
			Clock clock,
			BlogManager blogManager,
			InvitationFactory<Blog, BlogInvitationResponse> invitationFactory) {
		super(db, clientHelper, clientVersioningManager, messageEncoder,
				messageParser, autoDeleteManager,
				conversationManager, clock, BlogSharingManager.CLIENT_ID,
				BlogSharingManager.MAJOR_VERSION, BlogManager.CLIENT_ID,
				BlogManager.MAJOR_VERSION);
		this.blogManager = blogManager;
		this.invitationFactory = invitationFactory;
	}

	@Override
	Event getInvitationRequestReceivedEvent(InviteMessage<Blog> m,
			ContactId contactId, boolean available, boolean canBeOpened) {
		ConversationRequest<Blog> request = invitationFactory
				.createInvitationRequest(false, false, true, false, m,
						contactId, available, canBeOpened,
						m.getAutoDeleteTimer());
		return new BlogInvitationRequestReceivedEvent(request, contactId);
	}

	@Override
	Event getInvitationResponseReceivedEvent(AcceptMessage m,
			ContactId contactId) {
		BlogInvitationResponse response = invitationFactory
				.createInvitationResponse(m.getId(), m.getContactGroupId(),
						m.getTimestamp(), false, false, false, false,
						true, m.getShareableId(), m.getAutoDeleteTimer(),
						false);
		return new BlogInvitationResponseReceivedEvent(response, contactId);
	}

	@Override
	Event getInvitationResponseReceivedEvent(DeclineMessage m,
			ContactId contactId) {
		BlogInvitationResponse response = invitationFactory
				.createInvitationResponse(m.getId(), m.getContactGroupId(),
						m.getTimestamp(), false, false, false, false,
						false, m.getShareableId(), m.getAutoDeleteTimer(),
						false);
		return new BlogInvitationResponseReceivedEvent(response, contactId);
	}

	@Override
	Event getAutoDeclineInvitationResponseReceivedEvent(Session s, Message m,
			ContactId contactId, long timer) {
		BlogInvitationResponse response = invitationFactory
				.createInvitationResponse(m.getId(), s.getContactGroupId(),
						m.getTimestamp(), true, false, false, true,
						false, s.getShareableId(), timer, true);
		return new BlogInvitationResponseReceivedEvent(response, contactId);
	}

	@Override
	protected void addShareable(Transaction txn, MessageId inviteId)
			throws DbException, FormatException {
		InviteMessage<Blog> invite =
				messageParser.getInviteMessage(txn, inviteId);
		blogManager.addBlog(txn, invite.getShareable());
	}

}




