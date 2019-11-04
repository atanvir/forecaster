package com.Bforecaster.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class AvenirBoldTextView extends AppCompatTextView {
    public AvenirBoldTextView(Context context) {
        super(context);
        createFont();
    }

    public AvenirBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public AvenirBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        setTypeface(font);
    }
}
