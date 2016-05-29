package com.tlabs.android.jeeves.views.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TextProgressBar extends LinearLayout {

	private TextView textView;
	private ProgressBar progressView;
    private Drawable background;

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextProgressBar);
        this.background = a.getDrawable(R.styleable.TextProgressBar_progressBackground);
        a.recycle();
	}

	public TextProgressBar(Context context) {
		super(context);
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		
		View.inflate(
                getContext().getApplicationContext(),
                R.layout.jeeves_view_textprogress,
                this);
		final ViewGroup parent = (ViewGroup)getChildAt(0);
        parent.setVisibility(VISIBLE);
		this.progressView = (ProgressBar)parent.getChildAt(0);

        this.progressView.setMax(100);
        this.progressView.setProgress(0);
        if (null != this.background) {
            this.progressView.setProgressDrawable(this.background);
        }
		this.textView = (TextView)parent.getChildAt(1);
        //this.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, parent.getHeight());
    }

    public void setProgress(final int progress) {
        this.progressView.setProgress(progress);
        this.textView.setText(progress + "%");
    }

    public void setTextVisible(boolean visible) {
        this.textView.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public TextView getTextView() {
        return textView;
    }

    public ProgressBar getProgressView() {
        return progressView;
    }
}
