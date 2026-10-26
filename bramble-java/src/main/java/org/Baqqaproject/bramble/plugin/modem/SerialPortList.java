package org.Baqqaproject.bramble.plugin.modem;

import org.briarproject.nullsafety.NotNullByDefault;

@NotNullByDefault
interface SerialPortList {

	String[] getPortNames();
}




