package org.Baqqaproject.Baqqa.api.attachment;

import static org.Baqqaproject.bramble.api.sync.SyncConstants.MAX_MESSAGE_BODY_LENGTH;

public interface MediaConstants {

	// Metadata keys for single-message attachments
	String MSG_KEY_CONTENT_TYPE = "contentType";
	String MSG_KEY_DESCRIPTOR_LENGTH = "descriptorLength";

	// Metadata keys for chunked attachments
	String MSG_KEY_ATTACHMENT_ID = "attachmentId";
	String MSG_KEY_CHUNK_INDEX = "chunkIndex";
	String MSG_KEY_TOTAL_CHUNKS = "totalChunks";
	String MSG_KEY_TOTAL_SIZE = "totalSize";
	String MSG_KEY_FILE_NAME = "fileName";

	/**
	 * The maximum length of an attachment's content type in UTF-8 bytes.
	 */
	int MAX_CONTENT_TYPE_BYTES = 80;

	/**
	 * The maximum length of an attachment's original file name in UTF-8 bytes.
	 */
	int MAX_FILE_NAME_BYTES = 255;

	/**
	 * The maximum allowed size of image attachments.
	 * TODO: Different limit for GIFs?
	 */
	int MAX_IMAGE_SIZE = MAX_MESSAGE_BODY_LENGTH - 100;

	/**
	 * The maximum size of a single-message attachment (that is not chunked).
	 * The attachment descriptor plus the payload must remain below
	 * {@link org.Baqqaproject.bramble.api.sync.SyncConstants#MAX_MESSAGE_BODY_LENGTH}.
	 */
	int MAX_SINGLE_ATTACHMENT_SIZE = MAX_MESSAGE_BODY_LENGTH - 100;

	/**
	 * The number of elements in a chunked attachment message's descriptor:
	 * message type, content type, attachment ID, chunk index, total chunks,
	 * total size, optional file name.
	 */
	int CHUNKED_DESCRIPTOR_SIZE = 7;

	/**
	 * The maximum useful data payload of a single attachment chunk, in bytes.
	 * The chunk descriptor plus this payload must remain comfortably below
	 * {@link org.Baqqaproject.bramble.api.sync.SyncConstants#MAX_MESSAGE_BODY_LENGTH}.
	 */
	int MAX_ATTACHMENT_CHUNK_SIZE = 30 * 1024; // 30 KiB

	/**
	 * The maximum number of chunks allowed per logical attachment.
	 */
	int MAX_TOTAL_CHUNKS = 4096;

	/**
	 * The maximum total size of a logical (chunked) attachment, in bytes.
	 */
	long MAX_TOTAL_ATTACHMENT_SIZE = 128L * 1024 * 1024; // 128 MiB
}



