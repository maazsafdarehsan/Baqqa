package org.Baqqaproject.Baqqa.privategroup.invitation;

import org.Baqqaproject.bramble.api.sync.Group.Visibility;

interface State {

	int getValue();

	Visibility getVisibility();

	boolean isAwaitingResponse();
}



