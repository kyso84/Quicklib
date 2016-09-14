package com.quicklib.android.helper;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author bdescha1
 * @since 16-07-25
 * Copyright (C) 2016 French Connection !!!
 */
public class ViewHelper {

    public static void tint(EditText editText, @ColorRes int colorId) {
        tint((TextView) editText, colorId);
    }

    public static void tint(TextView textView,@ColorRes int colorId) {
        Drawable[] newDrawable = new Drawable[4];
        int x = 0;
        for (Drawable drawable : textView.getCompoundDrawables()) {
            newDrawable[x] = DrawableHelper.getTintedDrawable(textView.getContext(), drawable, colorId);
            x++;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(newDrawable[0], newDrawable[1], newDrawable[2], newDrawable[3]);
    }

}