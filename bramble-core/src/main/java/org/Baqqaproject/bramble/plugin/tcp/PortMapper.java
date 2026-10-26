package org.Baqqaproject.bramble.plugin.tcp;

import javax.annotation.Nullable;

interface PortMapper {

	@Nullable
	MappingResult map(int port);
}



