package com.Bforecaster.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class AvenirDemiEditText extends AppCompatEditText {
    public AvenirDemiEditText(Context context) {
        super(context);
        createFont();
    }

    public AvenirDemiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirDemiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }
    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Demi.otf");
        setTypeface(font);
    }
}
