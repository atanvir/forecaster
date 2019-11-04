package com.Bforecaster.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class AvenirBoldButton extends AppCompatButton {
    public AvenirBoldButton(Context context) {
        super(context);
        createFont();
    }

    public AvenirBoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirBoldButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }



    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        setTypeface(font);
    }
}
