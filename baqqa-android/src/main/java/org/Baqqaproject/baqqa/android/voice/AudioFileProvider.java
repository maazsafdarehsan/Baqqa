package org.Baqqaproject.Baqqa.android.voice;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import org.briarproject.nullsafety.NotNullByDefault;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Exposes a freshly recorded voice note to the rest of the app through the
 * content resolver, so the existing attachment pipeline
 * ({@code AttachmentCreationTask}) can resolve the MIME type and file name of
 * a recording without any changes.
 * <p>
 * The provider only reads files from the private cache directory, exposing a
 * single file per URI. It never grants or exposes anything to other apps.
 */
@NotNullByDefault
public class AudioFileProvider extends ContentProvider {

	private static final String AUTHORITY_SUFFIX = ".audioprovider";

	@Nullable
	private File baseDir;

	/**
	 * Builds a content URI for the given recorded file. The file must live in
	 * the app's cache voice-notes directory.
	 */
	public static Uri getUriForFile(Context ctx, File file) {
		String authority = ctx.getPackageName() + AUTHORITY_SUFFIX;
		return Uri.parse("content://" + authority + "/" +
				Uri.encode(file.getName()));
	}

	@Override
	public boolean onCreate() {
		Context ctx = getContext();
		if (ctx != null) {
			baseDir = new File(ctx.getCacheDir(), VoiceNote.DIRECTORY);
		}
		return true;
	}

	@Override
	@Nullable
	public String getType(@NonNull Uri uri) {
		return VoiceNote.MIME_TYPE;
	}

	@Override
	@Nullable
	public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode)
			throws FileNotFoundException {
		if (!"r".equals(mode)) throw new FileNotFoundException("Read-only");
		File file = resolveFile(uri);
		return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
	}

	/**
	 * OpenableColumns only defines DISPLAY_NAME and SIZE; the MIME type is
	 * reported via its well-known column name.
	 */
	private static final String COLUMN_MIME_TYPE = "mime_type";

	@Override
	@Nullable
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
			@Nullable String selection, @Nullable String[] selectionArgs,
			@Nullable String sortOrder) {
		File file;
		try {
			file = resolveFile(uri);
		} catch (FileNotFoundException e) {
			return null;
		}
		MatrixCursor cursor =
				new MatrixCursor(new String[] {OpenableColumns.DISPLAY_NAME,
						OpenableColumns.SIZE, COLUMN_MIME_TYPE});
		cursor.addRow(new Object[] {VoiceNote.FILE_NAME, file.length(),
				VoiceNote.MIME_TYPE});
		return cursor;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		return null;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection,
			@Nullable String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values,
			@Nullable String selection, @Nullable String[] selectionArgs) {
		return 0;
	}

	/**
	 * Resolves a URI of the form {@code content://<authority>/<file name>} to
	 * a file inside the base directory, rejecting any path traversal.
	 */
	private File resolveFile(Uri uri) throws FileNotFoundException {
		File baseDir = this.baseDir;
		if (baseDir == null) throw new FileNotFoundException("No base dir");
		String name = uri.getLastPathSegment();
		if (name == null || name.contains("/") || name.contains("\\")) {
			throw new FileNotFoundException("Invalid path");
		}
		File file = new File(baseDir, Uri.decode(name));
		if (!file.getParentFile().equals(baseDir)) {
			throw new FileNotFoundException("Invalid path");
		}
		if (!file.isFile()) throw new FileNotFoundException("No such file");
		return file;
	}

}
