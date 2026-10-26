package org.Baqqaproject.Baqqa.identity;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.db.DatabaseComponent;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.identity.AuthorId;
import org.Baqqaproject.bramble.api.identity.IdentityManager;
import org.Baqqaproject.bramble.api.identity.LocalAuthor;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.avatar.AvatarManager;
import org.Baqqaproject.Baqqa.api.identity.AuthorInfo;
import org.Baqqaproject.Baqqa.api.identity.AuthorManager;
import org.briarproject.nullsafety.NotNullByDefault;

import java.util.Collection;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

import static org.Baqqaproject.Baqqa.api.identity.AuthorInfo.Status.OURSELVES;
import static org.Baqqaproject.Baqqa.api.identity.AuthorInfo.Status.UNKNOWN;
import static org.Baqqaproject.Baqqa.api.identity.AuthorInfo.Status.UNVERIFIED;
import static org.Baqqaproject.Baqqa.api.identity.AuthorInfo.Status.VERIFIED;

@ThreadSafe
@NotNullByDefault
class AuthorManagerImpl implements AuthorManager {

	private final DatabaseComponent db;
	private final IdentityManager identityManager;
	private final AvatarManager avatarManager;

	@Inject
	AuthorManagerImpl(DatabaseComponent db, IdentityManager identityManager,
			AvatarManager avatarManager) {
		this.db = db;
		this.identityManager = identityManager;
		this.avatarManager = avatarManager;
	}

	@Override
	public AuthorInfo getAuthorInfo(AuthorId a) throws DbException {
		return db.transactionWithResult(true, txn -> getAuthorInfo(txn, a));
	}

	@Override
	public AuthorInfo getAuthorInfo(Transaction txn, AuthorId authorId)
			throws DbException {
		LocalAuthor localAuthor = identityManager.getLocalAuthor(txn);
		if (localAuthor.getId().equals(authorId)) return getMyAuthorInfo(txn);
		Collection<Contact> contacts = db.getContactsByAuthorId(txn, authorId);
		if (contacts.isEmpty()) return new AuthorInfo(UNKNOWN);
		if (contacts.size() > 1) throw new AssertionError();
		Contact c = contacts.iterator().next();
		return getAuthorInfo(txn, c);
	}

	@Override
	public AuthorInfo getAuthorInfo(Contact c) throws DbException {
		return db.transactionWithResult(true, txn -> getAuthorInfo(txn, c));
	}

	@Override
	public AuthorInfo getAuthorInfo(Transaction txn, Contact c)
			throws DbException {
		AttachmentHeader avatar = avatarManager.getAvatarHeader(txn, c);
		if (c.isVerified())
			return new AuthorInfo(VERIFIED, c.getAlias(), avatar);
		else return new AuthorInfo(UNVERIFIED, c.getAlias(), avatar);
	}

	@Override
	public AuthorInfo getMyAuthorInfo() throws DbException {
		return db.transactionWithResult(true, this::getMyAuthorInfo);
	}

	@Override
	public AuthorInfo getMyAuthorInfo(Transaction txn) throws DbException {
		AttachmentHeader avatar = avatarManager.getMyAvatarHeader(txn);
		return new AuthorInfo(OURSELVES, null, avatar);
	}

}




