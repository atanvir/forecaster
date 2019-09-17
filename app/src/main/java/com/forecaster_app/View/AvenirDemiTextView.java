package com.forecaster_app.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class AvenirDemiTextView extends AppCompatTextView {
    public AvenirDemiTextView(Context context) {
        super(context);
        createFont();

    }

    public AvenirDemiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirDemiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Demi.otf");
        setTypeface(font);
    }
}
