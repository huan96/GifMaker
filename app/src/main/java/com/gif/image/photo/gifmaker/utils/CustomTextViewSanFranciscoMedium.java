package com.gif.image.photo.gifmaker.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextViewSanFranciscoMedium extends TextView {
    public CustomTextViewSanFranciscoMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewSanFranciscoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewSanFranciscoMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("SanFranciscoMedium.ttf", context);
        setTypeface(customFont);
    }
}
