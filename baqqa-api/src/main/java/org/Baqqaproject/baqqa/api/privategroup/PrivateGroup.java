package org.Baqqaproject.Baqqa.api.privategroup;

import org.Baqqaproject.bramble.api.identity.Author;
import org.Baqqaproject.bramble.api.sync.Group;
import org.Baqqaproject.Baqqa.api.client.NamedGroup;
import org.Baqqaproject.Baqqa.api.sharing.Shareable;
import org.briarproject.nullsafety.NotNullByDefault;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class PrivateGroup extends NamedGroup implements Shareable {

	private final Author creator;

	public PrivateGroup(Group group, String name, Author creator, byte[] salt) {
		super(group, name, salt);
		this.creator = creator;
	}

	public Author getCreator() {
		return creator;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof PrivateGroup && super.equals(o);
	}

}




