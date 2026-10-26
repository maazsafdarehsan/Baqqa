package org.Baqqaproject.Baqqa.android.privategroup.memberlist;

import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.controller.DbController;
import org.Baqqaproject.Baqqa.android.controller.handler.ResultExceptionHandler;

import java.util.Collection;

public interface GroupMemberListController extends DbController {

	void loadMembers(GroupId groupId,
			ResultExceptionHandler<Collection<MemberListItem>, DbException> handler);

}



