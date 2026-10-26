package org.Baqqaproject.Baqqa.sharing;

import org.Baqqaproject.bramble.api.FormatException;
import org.Baqqaproject.bramble.api.client.ClientHelper;
import org.Baqqaproject.bramble.api.data.BdfList;
import org.Baqqaproject.bramble.api.data.MetadataEncoder;
import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.system.Clock;
import org.Baqqaproject.Baqqa.api.blog.BlogFactory;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

import static org.Baqqaproject.bramble.util.ValidationUtils.checkSize;

@Immutable
@NotNullByDefault
class BlogSharingValidator extends SharingValidator {

	private final BlogFactory blogFactory;

	BlogSharingValidator(MessageEncoder messageEncoder,
			ClientHelper clientHelper, MetadataEncoder metadataEncoder,
			Clock clock, BlogFactory blogFactory) {
		super(messageEncoder, clientHelper, metadataEncoder, clock);
		this.blogFactory = blogFactory;
	}

	@Override
	protected GroupId validateDescriptor(BdfList descriptor)
			throws FormatException {
		// Author, RSS
		checkSize(descriptor, 2);
		BdfList authorList = descriptor.getList(0);
		boolean rssFeed = descriptor.getBoolean(1);
		Author author = clientHelper.parseAndValidateAuthor(authorList);
		if (rssFeed) return blogFactory.createFeedBlog(author).getId();
		else return blogFactory.createBlog(author).getId();
	}

}




