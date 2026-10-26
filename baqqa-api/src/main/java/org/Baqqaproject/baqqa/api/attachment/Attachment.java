package org.Baqqaproject.Baqqa.api.attachment;

import org.briarproject.nullsafety.NotNullByDefault;

import java.io.InputStream;

import javax.annotation.concurrent.Immutable;

@Immutable
@NotNullByDefault
public class Attachment {

	private final AttachmentHeader header;
	private final InputStream stream;
	private final long totalSize;

	public Attachment(AttachmentHeader header, InputStream stream) {
		this(header, stream, -1);
	}

	public Attachment(AttachmentHeader header, InputStream stream,
			long totalSize) {
		this.header = header;
		this.stream = stream;
		this.totalSize = totalSize;
	}

	public AttachmentHeader getHeader() {
		return header;
	}

	public InputStream getStream() {
		return stream;
	}

	/**
	 * Returns the total size of the attachment's data in bytes, or -1 if the
	 * size is not known.
	 */
	public long getTotalSize() {
		return totalSize;
	}

}




