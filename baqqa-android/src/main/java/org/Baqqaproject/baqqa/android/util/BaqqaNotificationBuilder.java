package org.Baqqaproject.Baqqa.android.util;

import android.content.Context;

import org.Baqqaproject.Baqqa.R;

import androidx.annotation.ColorRes;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import static androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE;

public class BaqqaNotificationBuilder extends NotificationCompat.Builder {

	private final Context context;

	public BaqqaNotificationBuilder(Context context, String channelId) {
		super(context, channelId);
		this.context = context;
		// Auto-cancel does not fire the delete intent, see
		// https://issuetracker.google.com/issues/36961721
		setAutoCancel(true);

		setLights(ContextCompat.getColor(context, R.color.baqqa_lime_400),
				750, 500);
		setVisibility(VISIBILITY_PRIVATE);
	}

	public BaqqaNotificationBuilder setColorRes(@ColorRes int res) {
		setColor(ContextCompat.getColor(context, res));
		return this;
	}

	public BaqqaNotificationBuilder setNotificationCategory(String category) {
		setCategory(category);
		return this;
	}

}



