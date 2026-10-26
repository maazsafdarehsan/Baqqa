package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.sharing.Shareable;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface MessageParser<S extends Shareable> {

	BdfDictionary getMessagesVisibleInUiQuery();

	BdfDictionary getInvitesAvailableToAnswerQuery();

	BdfDictionary getInvitesAvailableToAnswerQuery(GroupId shareableId);

	MessageMetadata parseMetadata(BdfDictionary meta) throws FormatException;

	S createShareable(BdfList descriptor) throws FormatException;

	InviteMessage<S> getInviteMessage(Transaction txn, MessageId m)
			throws DbException, FormatException;

	InviteMessage<S> parseInviteMessage(Message m, BdfList body)
			throws FormatException;

	AcceptMessage parseAcceptMessage(Message m, BdfList body)
			throws FormatException;

	DeclineMessage parseDeclineMessage(Message m, BdfList body)
			throws FormatException;

	LeaveMessage parseLeaveMessage(Message m, BdfList body)
			throws FormatException;

	AbortMessage parseAbortMessage(Message m, BdfList body)
			throws FormatException;

}




