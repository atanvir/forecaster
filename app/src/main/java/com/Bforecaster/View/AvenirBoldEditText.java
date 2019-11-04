package com.Bforecaster.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class AvenirBoldEditText extends AppCompatEditText {
    public AvenirBoldEditText(Context context) {
        super(context);
        createFont();

    }

    public AvenirBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirBoldEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        setTypeface(font);
    }
}
