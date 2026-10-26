package org.Baqqaproject.Baqqa.android.voice;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VoiceNoteTest {

	@Test
	public void testFormatSecondsZero() {
		assertEquals("0:00", VoiceNote.formatSeconds(0));
	}

	@Test
	public void testFormatSecondsSingleDigits() {
		assertEquals("0:07", VoiceNote.formatSeconds(7));
	}

	@Test
	public void testFormatSecondsWithMinutes() {
		assertEquals("1:23", VoiceNote.formatSeconds(83));
	}

	@Test
	public void testFormatSecondsTenMinutes() {
		assertEquals("10:00", VoiceNote.formatSeconds(600));
	}

	@Test
	public void testFormatSecondsNegativeClampsToZero() {
		assertEquals("0:00", VoiceNote.formatSeconds(-5));
	}

	@Test
	public void testOutputFileHasM4aSuffixAndTimestamp() {
		File dir = new File("whatever");
		File file = VoiceNote.getOutputFile(dir, 1234567890L);
		assertTrue(file.getPath().startsWith(dir.getPath()));
		assertTrue(file.getName().startsWith("voice_note_"));
		assertTrue(file.getName().endsWith(".m4a"));
		assertEquals("voice_note_1234567890.m4a", file.getName());
	}
}
