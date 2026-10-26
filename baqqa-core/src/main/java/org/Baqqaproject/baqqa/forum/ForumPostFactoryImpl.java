package org.Baqqaproject.Baqqa.forum;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.bramble.api.identity.LocalAuthor;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.Message;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.Baqqa.api.forum.ForumPost;
import org.Baqqaproject.Baqqa.api.forum.ForumPostFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import java.security.GeneralSecurityException;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.Baqqaproject.bramble.util.StringUtils.utf8IsTooLong;
import static org.Baqqaproject.Baqqa.api.forum.ForumConstants.MAX_FORUM_POST_TEXT_LENGTH;

@Immutable
@NotNullByDefault
class ForumPostFactoryImpl implements ForumPostFactory {

	private final ClientHelper clientHelper;

	@Inject
	ForumPostFactoryImpl(ClientHelper clientHelper) {
		this.clientHelper = clientHelper;
	}

	@Override
	public ForumPost createPost(GroupId groupId, long timestamp,
			@Nullable MessageId parent, LocalAuthor author, String text)
			throws FormatException, GeneralSecurityException {
		// Validate the arguments
		if (utf8IsTooLong(text, MAX_FORUM_POST_TEXT_LENGTH))
			throw new IllegalArgumentException();
		// Serialise the data to be signed
		BdfList authorList = clientHelper.toList(author);
		BdfList signed = BdfList.of(groupId, timestamp, parent, authorList,
				text);
		// Sign the data
		byte[] sig = clientHelper.sign(SIGNING_LABEL_POST, signed,
				author.getPrivateKey());
		// Serialise the signed message
		BdfList message = BdfList.of(parent, authorList, text, sig);
		Message m = clientHelper.createMessage(groupId, timestamp, message);
		return new ForumPost(m, parent, author);
	}

}




