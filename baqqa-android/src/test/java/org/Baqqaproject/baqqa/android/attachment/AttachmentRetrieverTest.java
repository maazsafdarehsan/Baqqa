package org.Baqqaproject.Baqqa.android.attachment;

import org.Baqqaproject.bramble.api.sync.GroupId;
import org.Baqqaproject.bramble.api.sync.MessageId;
import org.Baqqaproject.bramble.test.BrambleMockTestCase;
import org.Baqqaproject.bramble.test.ImmediateExecutor;
import org.Baqqaproject.Baqqa.android.attachment.media.ImageHelper;
import org.Baqqaproject.Baqqa.android.attachment.media.ImageSizeCalculator;
import org.Baqqaproject.Baqqa.android.attachment.media.Size;
import org.Baqqaproject.Baqqa.api.attachment.Attachment;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentHeader;
import org.Baqqaproject.Baqqa.api.attachment.AttachmentReader;
import org.jmock.Expectations;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Executor;

import static org.Baqqaproject.bramble.test.TestUtils.getRandomBytes;
import static org.Baqqaproject.bramble.test.TestUtils.getRandomId;
import static org.Baqqaproject.Baqqa.android.attachment.AttachmentItem.State.AVAILABLE;
import static org.Baqqaproject.Baqqa.android.attachment.AttachmentItem.State.ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttachmentRetrieverTest extends BrambleMockTestCase {

	private final AttachmentDimensions dimensions = new AttachmentDimensions(
			100, 50, 200, 75, 300
	);
	private final GroupId groupId = new GroupId(getRandomId());
	private final MessageId msgId = new MessageId(getRandomId());
	private final ImageHelper imageHelper = context.mock(ImageHelper.class);
	private final ImageSizeCalculator imageSizeCalculator;
	private final AttachmentRetriever retriever;

	public AttachmentRetrieverTest() {
		context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
		AttachmentReader attachmentReader =
				context.mock(AttachmentReader.class);
		imageSizeCalculator = context.mock(ImageSizeCalculator.class);
		Executor dbExecutor = new ImmediateExecutor();
		retriever = new AttachmentRetrieverImpl(dbExecutor, attachmentReader,
				dimensions, imageHelper, imageSizeCalculator);
	}

	@Test
	public void testNoSize() {
		String mimeType = "image/jpeg";
		Attachment attachment = getAttachment(mimeType);

		context.checking(new Expectations() {{
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue("jpg"));
		}});

		AttachmentItem item = retriever.createAttachmentItem(attachment, false);
		assertEquals(mimeType, item.getMimeType());
		assertEquals("jpg", item.getExtension());
		assertEquals(AVAILABLE, item.getState());
	}

	@Test
	public void testNoSizeWrongMimeTypeProducesError() {
		String mimeType = "application/octet-stream";
		Attachment attachment = getAttachment(mimeType);

		context.checking(new Expectations() {{
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue(null));
		}});

		// generic file with unknown extension is still AVAILABLE
		// (it can be opened by an external app)
		AttachmentItem item = retriever.createAttachmentItem(attachment, false);
		assertEquals(AVAILABLE, item.getState());
		assertEquals("", item.getExtension());
	}

	@Test
	public void testGenericFileIsAvailable() {
		String mimeType = "application/pdf";
		Attachment attachment = getAttachment(mimeType);

		context.checking(new Expectations() {{
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue("pdf"));
		}});

		// generic file attachment is AVAILABLE (not ERROR)
		AttachmentItem item = retriever.createAttachmentItem(attachment, false);
		assertEquals(AVAILABLE, item.getState());
		assertEquals("pdf", item.getExtension());
		assertEquals(mimeType, item.getMimeType());
	}

	@Test
	public void testSmallJpegImage() {
		String mimeType = "image/jpeg";
		Attachment attachment = getAttachment(mimeType);

		context.checking(new Expectations() {{
			oneOf(imageSizeCalculator).getSize(with(any(InputStream.class)),
					with(mimeType));
			will(returnValue(new Size(160, 240, mimeType)));
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue("jpg"));
		}});

		AttachmentItem item = retriever.createAttachmentItem(attachment, true);
		assertEquals(msgId, item.getMessageId());
		assertEquals(160, item.getWidth());
		assertEquals(240, item.getHeight());
		assertEquals(160, item.getThumbnailWidth());
		assertEquals(240, item.getThumbnailHeight());
		assertEquals(mimeType, item.getMimeType());
		assertEquals("jpg", item.getExtension());
		assertEquals(AVAILABLE, item.getState());
	}

	@Test
	public void testBigJpegImage() {
		String mimeType = "image/jpeg";
		Attachment attachment = getAttachment(mimeType);

		context.checking(new Expectations() {{
			oneOf(imageSizeCalculator).getSize(with(any(InputStream.class)),
					with(mimeType));
			will(returnValue(new Size(1728, 2592, mimeType)));
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue("jpg"));
		}});

		AttachmentItem item = retriever.createAttachmentItem(attachment, true);
		assertEquals(1728, item.getWidth());
		assertEquals(2592, item.getHeight());
		assertEquals(dimensions.maxWidth, item.getThumbnailWidth());
		assertEquals(dimensions.maxHeight, item.getThumbnailHeight());
		assertEquals(AVAILABLE, item.getState());
	}

	@Test
	public void testMalformedError() {
		String mimeType = "image/jpeg";
		Attachment attachment = getAttachment(mimeType);

		context.checking(new Expectations() {{
			oneOf(imageSizeCalculator).getSize(with(any(InputStream.class)),
					with(mimeType));
			will(returnValue(new Size()));
			oneOf(imageHelper).getExtensionFromMimeType("");
			will(returnValue(null));
		}});

		AttachmentItem item = retriever.createAttachmentItem(attachment, true);
		assertEquals(ERROR, item.getState());
	}

	@Test
	public void testGenericFileDoesNotNeedImageSize() {
		String mimeType = "application/pdf";
		String fileName = "document.pdf";
		AttachmentHeader header =
				new AttachmentHeader(groupId, msgId, mimeType, fileName,
						1024);
		Attachment attachment = new Attachment(header,
				new ByteArrayInputStream(getRandomBytes(42)));

		// no imageSizeCalculator call expected for non-image files
		context.checking(new Expectations() {{
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue("pdf"));
		}});

		AttachmentItem item =
				retriever.createAttachmentItem(attachment, true);
		assertEquals(mimeType, item.getMimeType());
		assertEquals(fileName, item.getFileName());
		assertEquals(1024, item.getTotalSize());
		assertEquals("pdf", item.getExtension());
		assertEquals(AVAILABLE, item.getState());
		assertFalse(item.isImage());
	}

	@Test
	public void testGenericFileWithUnknownMimeIsAvailable() {
		String mimeType = "application/octet-stream";
		AttachmentHeader header =
				new AttachmentHeader(groupId, msgId, mimeType, "file.bin",
						2048);
		Attachment attachment = new Attachment(header,
				new ByteArrayInputStream(getRandomBytes(42)));

		context.checking(new Expectations() {{
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue("bin"));
		}});

		AttachmentItem item =
				retriever.createAttachmentItem(attachment, false);
		assertEquals(AVAILABLE, item.getState());
		assertEquals("bin", item.getExtension());
		assertEquals(2048, item.getTotalSize());
		assertFalse(item.isImage());
	}

	@Test
	public void testGenericFileWithNoExtensionIsAvailable() {
		String mimeType = "application/octet-stream";
		AttachmentHeader header =
				new AttachmentHeader(groupId, msgId, mimeType, null, -1);
		Attachment attachment = new Attachment(header,
				new ByteArrayInputStream(getRandomBytes(42)));

		context.checking(new Expectations() {{
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue(null));
		}});

		AttachmentItem item =
				retriever.createAttachmentItem(attachment, false);
		assertEquals(AVAILABLE, item.getState());
		assertEquals("", item.getExtension());
		assertEquals(-1, item.getTotalSize());
		assertFalse(item.isImage());
	}

	@Test
	public void testImageAttachmentStillUsesImagePath() {
		String mimeType = "image/png";
		Attachment attachment = getAttachment(mimeType);

		context.checking(new Expectations() {{
			oneOf(imageSizeCalculator).getSize(with(any(InputStream.class)),
					with(mimeType));
			will(returnValue(new Size(100, 200, mimeType)));
			oneOf(imageHelper).getExtensionFromMimeType(mimeType);
			will(returnValue("png"));
		}});

		AttachmentItem item =
				retriever.createAttachmentItem(attachment, true);
		assertTrue(item.isImage());
		assertEquals(100, item.getWidth());
		assertEquals(200, item.getHeight());
		assertEquals(AVAILABLE, item.getState());
	}

	private Attachment getAttachment(String contentType) {
		AttachmentHeader header =
				new AttachmentHeader(groupId, msgId, contentType);
		InputStream in = new ByteArrayInputStream(getRandomBytes(42));
		return new Attachment(header, in);
	}
}



