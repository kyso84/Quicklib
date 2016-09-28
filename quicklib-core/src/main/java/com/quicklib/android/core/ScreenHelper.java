package com.quicklib.android.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;


/**
 * This class provides useful features to screen characteristics
 *
 * @author Benoit Deschanel
 * @since 16-09-26
 * Copyright (C) 2016 Quicklib
 */
public class ScreenHelper {

    /**
     * This method returns a ScreenSize class
     *
     * @param context application's context
     * @return ScreenSize object
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static ScreenSize getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        float density = context.getResources().getDisplayMetrics().density;
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        return new ScreenSize(density, size.x, size.y);
    }



    public static class ScreenSize {
        private float density = 0;
        private float width = 0;
        private float height = 0;
        private float dpWidth = 0;
        private float dpHeight = 0;


        public ScreenSize(float density, float width, float height) {
            super();
            this.density = density;
            this.width = width;
            this.height = height;
            this.dpWidth = width / density;
            this.dpHeight = height / density;
        }

        public float getDp(int pixel) {
            return pixel / density;
        }

        public float getPixel(int dp) {
            return dp * density;
        }

        public float getDensity() {
            return density;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public float getDpWidth() {
            return dpWidth;
        }

        public float getDpHeight() {
            return dpHeight;
        }

    }
}
