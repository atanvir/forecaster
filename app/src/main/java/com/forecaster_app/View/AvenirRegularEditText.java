package com.forecaster_app.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class AvenirRegularEditText extends AppCompatEditText {
    public AvenirRegularEditText(Context context) {
        super(context);
        createFont();

    }

    public AvenirRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirRegularEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setTypeface(font);
    }
}
