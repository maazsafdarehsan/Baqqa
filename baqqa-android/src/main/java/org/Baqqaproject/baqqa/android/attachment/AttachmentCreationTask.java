package org.Baqqaproject.Baqqa.android.attachment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import org.Baqqaproject.bramble.api.db.DbException;
import org.Baqqaproject.bramble.api.lifecycle.IoExecutor;
import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.Baqqa.android.attachment.media.ImageCompressor;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.messaging.MessagingManager;
import org.briarproject.nullsafety.NotNullByDefault;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Logger;

import androidx.annotation.Nullable;

import static java.util.Arrays.asList;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static org.Baqqaproject.bramble.util.AndroidUtils.getSupportedImageContentTypes;
import static org.Baqqaproject.bramble.util.IoUtils.tryToClose;
import static org.Baqqaproject.bramble.util.LogUtils.logDuration;
import static org.Baqqaproject.bramble.util.LogUtils.logException;
import static org.Baqqaproject.bramble.util.LogUtils.now;
import static org.Baqqaproject.Baqqa.android.attachment.media.ImageCompressor.MIME_TYPE;

@NotNullByDefault
class AttachmentCreationTask {

	private static final Logger LOG =
			getLogger(AttachmentCreationTask.class.getName());

	private final MessagingManager messagingManager;
	private final ContentResolver contentResolver;
	private final ImageCompressor imageCompressor;
	private final GroupId groupId;
	private final Collection<Uri> uris;
	private final boolean needsSize;
	@Nullable
	private volatile AttachmentCreator attachmentCreator;

	private volatile boolean canceled = false;

	AttachmentCreationTask(MessagingManager messagingManager,
			ContentResolver contentResolver,
			AttachmentCreator attachmentCreator,
			ImageCompressor imageCompressor,
			GroupId groupId, Collection<Uri> uris, boolean needsSize) {
		this.messagingManager = messagingManager;
		this.contentResolver = contentResolver;
		this.imageCompressor = imageCompressor;
		this.groupId = groupId;
		this.uris = uris;
		this.needsSize = needsSize;
		this.attachmentCreator = attachmentCreator;
	}

	void cancel() {
		canceled = true;
		attachmentCreator = null;
	}

	@IoExecutor
	void storeAttachments() {
		for (Uri uri : uris) processUri(uri);
		AttachmentCreator attachmentCreator = this.attachmentCreator;
		if (!canceled && attachmentCreator != null)
			attachmentCreator.onAttachmentCreationFinished();
		this.attachmentCreator = null;
	}

	@IoExecutor
	private void processUri(Uri uri) {
		if (canceled) return;
		try {
			AttachmentHeader h = storeAttachment(uri);
			AttachmentCreator attachmentCreator = this.attachmentCreator;
			if (attachmentCreator != null) {
				attachmentCreator.onAttachmentHeaderReceived(uri, h, needsSize);
			}
		} catch (DbException | IOException e) {
			logException(LOG, WARNING, e);
			AttachmentCreator attachmentCreator = this.attachmentCreator;
			if (attachmentCreator != null) {
				attachmentCreator.onAttachmentError(uri, e);
			}
			canceled = true;
		}
	}

	@IoExecutor
	private AttachmentHeader storeAttachment(Uri uri)
			throws IOException, DbException {
		long start = now();
		String contentType = contentResolver.getType(uri);
		if (contentType == null) throw new IOException("null content type");
		// Images are compressed to a supported format; generic files are
		// passed through unchanged and the original MIME type is preserved.
		boolean isImage =
				asList(getSupportedImageContentTypes()).contains(contentType);
		InputStream is;
		try {
			is = contentResolver.openInputStream(uri);
			if (is == null) throw new IOException();
		} catch (SecurityException e) {
			throw new IOException(e);
		}
		String mimeType = contentType;
		if (isImage) {
			is = imageCompressor.compressImage(is, contentType);
			mimeType = MIME_TYPE;
		}
		long timestamp = System.currentTimeMillis();
		String fileName = getFileName(uri);
		AttachmentHeader h = messagingManager.addLocalAttachment(groupId,
				timestamp, mimeType, fileName, is);
		tryToClose(is, LOG, WARNING);
		logDuration(LOG, "Storing attachment", start);
		return h;
	}

	@IoExecutor
	@Nullable
	private String getFileName(Uri uri) {
		try (Cursor cursor =
					 contentResolver.query(uri, null, null, null, null)) {
			if (cursor == null || !cursor.moveToFirst()) return null;
			int nameIndex =
					cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
			if (nameIndex < 0) return null;
			String name = cursor.getString(nameIndex);
			return name == null || name.isEmpty() ? null : name;
		} catch (Exception e) {
			logException(LOG, WARNING, e);
			return null;
		}
	}

}




