package org.Baqqaproject.Baqqa.privategroup.invitation;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.data.BdfDictionary;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface MessageParser {

	BdfDictionary getMessagesVisibleInUiQuery();

	BdfDictionary getInvitesAvailableToAnswerQuery();

	BdfDictionary getInvitesAvailableToAnswerQuery(GroupId privateGroupId);

	MessageMetadata parseMetadata(BdfDictionary meta) throws FormatException;

	InviteMessage getInviteMessage(Transaction txn, MessageId m)
			throws DbException, FormatException;

	InviteMessage parseInviteMessage(Message m, BdfList body)
			throws FormatException;

	JoinMessage parseJoinMessage(Message m, BdfList body)
			throws FormatException;

	LeaveMessage parseLeaveMessage(Message m, BdfList body)
			throws FormatException;

	AbortMessage parseAbortMessage(Message m, BdfList body)
			throws FormatException;

}




