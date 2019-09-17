package com.forecaster_app.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class AvenirRegularTextView extends AppCompatTextView {
    public AvenirRegularTextView(Context context) {
        super(context);
        createFont();

    }

    public AvenirRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setTypeface(font);
    }
}
