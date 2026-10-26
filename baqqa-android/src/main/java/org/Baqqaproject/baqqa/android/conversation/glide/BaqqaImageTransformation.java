package org.Baqqaproject.Baqqa.android.conversation.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

public class BaqqaImageTransformation extends MultiTransformation<Bitmap> {

	public BaqqaImageTransformation(Radii r) {
		super(new CenterCrop(), new CustomCornersTransformation(r));
	}

}



