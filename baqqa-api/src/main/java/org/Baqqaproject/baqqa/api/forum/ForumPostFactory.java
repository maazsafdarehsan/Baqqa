package org.Baqqaproject.Baqqa.api.forum;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.crypto.CryptoExecutor;
import org.Baqqaproject.bramble.api.identity.LocalAuthor;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.briarproject.nullsafety.NotNullByDefault;

import java.security.GeneralSecurityException;

import javax.annotation.Nullable;

import static org.Baqqaproject.Baqqa.api.forum.ForumManager.CLIENT_ID;

@NotNullByDefault
public interface ForumPostFactory {

	String SIGNING_LABEL_POST = CLIENT_ID.getString() + "/POST";

	@CryptoExecutor
	ForumPost createPost(GroupId groupId, long timestamp,
			@Nullable MessageId parent, LocalAuthor author, String text)
			throws FormatException, GeneralSecurityException;

}




