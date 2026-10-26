package org.Baqqaproject.Baqqa.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;

import org.Baqqaproject.Baqqa.R;
import org.briarproject.nullsafety.NotNullByDefault;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.transition.TransitionManager.beginDelayedTransition;

@NotNullByDefault
public class BaqqaButton extends FrameLayout {

	private final Button button;
	private final ProgressBar progressBar;

	public BaqqaButton(Context context) {
		this(context, null);
	}

	public BaqqaButton(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaqqaButton(Context context, @Nullable AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.baqqa_button, this, true);

		TypedArray attributes =
				context.obtainStyledAttributes(attrs, R.styleable.BaqqaButton);
		String text = attributes.getString(R.styleable.BaqqaButton_text);
		int style = attributes
				.getResourceId(R.styleable.BaqqaButton_buttonStyle, 0);
		attributes.recycle();

		ContextThemeWrapper wrapper = new ContextThemeWrapper(context, style);
		button = new MaterialButton(wrapper, null, style);
		button.setText(text);
		addView(button);
		progressBar = findViewById(R.id.baqqa_button_progress_bar);
	}

	@Override
	public void setOnClickListener(@Nullable OnClickListener l) {
		if (l == null) button.setOnClickListener(null);
		else {
			button.setOnClickListener(v -> {
				beginDelayedTransition(this);
				progressBar.setVisibility(VISIBLE);
				button.setVisibility(INVISIBLE);
				l.onClick(this);
			});
		}
	}

	public void reset() {
		beginDelayedTransition(this);
		progressBar.setVisibility(INVISIBLE);
		button.setVisibility(VISIBLE);
	}

}




