package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.Baqqa.api.forum.Forum;
import org.Baqqaproject.Baqqa.api.forum.ForumFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

@Immutable
@NotNullByDefault
class ForumMessageParserImpl extends MessageParserImpl<Forum> {

	private final ForumFactory forumFactory;

	@Inject
	ForumMessageParserImpl(ClientHelper clientHelper,
			ForumFactory forumFactory) {
		super(clientHelper);
		this.forumFactory = forumFactory;
	}

	@Override
	public Forum createShareable(BdfList descriptor) throws FormatException {
		// Name, salt
		String name = descriptor.getString(0);
		byte[] salt = descriptor.getRaw(1);
		return forumFactory.createForum(name, salt);
	}

}




