package org.Baqqaproject.Baqqa.api.identity;

import org.Baqqaproject.bramble.api.contact.Contact;
import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.db.Transaction;
import org.Baqqaproject.bramble.api.identity.AuthorId;
import org.Baqqaproject.bramble.api.identity.LocalAuthor;
import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
public interface AuthorManager {

	/**
	 * Returns the {@link AuthorInfo} for the given author.
	 */
	AuthorInfo getAuthorInfo(AuthorId a) throws DbException;

	/**
	 * Returns the {@link AuthorInfo} for the given author.
	 */
	AuthorInfo getAuthorInfo(Transaction txn, AuthorId a) throws DbException;

	/**
	 * Returns the {@link AuthorInfo} for the given contact.
	 */
	AuthorInfo getAuthorInfo(Contact c) throws DbException;

	/**
	 * Returns the {@link AuthorInfo} for the given contact.
	 */
	AuthorInfo getAuthorInfo(Transaction txn, Contact c)
			throws DbException;

	/**
	 * Returns the {@link AuthorInfo} for the {@link LocalAuthor}.
	 */
	AuthorInfo getMyAuthorInfo() throws DbException;

	/**
	 * Returns the {@link AuthorInfo} for the {@link LocalAuthor}.
	 */
	AuthorInfo getMyAuthorInfo(Transaction txn) throws DbException;
}




