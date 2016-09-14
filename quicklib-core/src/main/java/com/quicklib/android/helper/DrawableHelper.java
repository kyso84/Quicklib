package com.quicklib.android.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.ypg.find.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author bdescha1
 * @since 16-07-25
 * Copyright (C) 2016 French Connection !!!
 */
public class DrawableHelper {
    public static Drawable getAssetImage(Context context, String imagePath) {
        try {
            InputStream ims = context.getAssets().open(imagePath);
            return Drawable.createFromStream(ims, null);
        } catch (IOException ex) {
            return ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        }
    }


    public static Drawable getTintedDrawable(Context context, @DrawableRes Drawable drawable, @ColorRes int colorId) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            try {
                DrawableCompat.setTintList(drawable.mutate(), ContextCompat.getColorStateList(context, colorId));
            } catch (Resources.NotFoundException e) {
                DrawableCompat.setTint(drawable.mutate(), ContextHelper.getColor(context, colorId));
            }
        }
        return drawable;
    }


    public static Drawable getTintedDrawable(Context context, @DrawableRes int drawableId, @ColorRes int colorId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        return getTintedDrawable(context, drawable, colorId);
    }


    public static Drawable getColoredDrawable(Context context, @DrawableRes Drawable drawable, @ColorInt int colorId) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            try {
                DrawableCompat.setTintList(drawable.mutate(), ContextCompat.getColorStateList(context, colorId));
            } catch (Resources.NotFoundException e) {
                DrawableCompat.setTint(drawable.mutate(), colorId);
            }
        }
        return drawable;
    }


    public static Drawable getColoredDrawable(Context context, @DrawableRes int drawableId, @ColorInt int colorId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        return getColoredDrawable(context, drawable, colorId);
    }

}