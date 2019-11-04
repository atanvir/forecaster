package com.Bforecaster.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class AvenirDemiButton extends AppCompatButton {
    public AvenirDemiButton(Context context) {
        super(context);
        createFont();
    }

    public AvenirDemiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirDemiButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }



    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Demi.otf");
        setTypeface(font);
    }
}

