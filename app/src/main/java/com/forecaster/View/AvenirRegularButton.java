package com.forecaster.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class AvenirRegularButton extends AppCompatButton {
    public AvenirRegularButton(Context context) {
        super(context);
        createFont();
    }

    public AvenirRegularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirRegularButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }


    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setTypeface(font);
    }
}
