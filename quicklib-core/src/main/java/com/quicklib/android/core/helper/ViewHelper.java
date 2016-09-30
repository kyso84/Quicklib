package com.quicklib.android.core.helper;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author bdescha1
 * @since 16-09-30
 * Copyright (C) 2016 French Connection !!!
 */
public class ViewHelper {


    public static void disable(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                for (int x = 0; x < ((ViewGroup) view).getChildCount(); x++) {
                    ViewHelper.disable(((ViewGroup) view).getChildAt(x));
                }
            } else {
                view.setEnabled(false);
            }
        }
    }


    public static void enable(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                for (int x = 0; x < ((ViewGroup) view).getChildCount(); x++) {
                    ViewHelper.enable(((ViewGroup) view).getChildAt(x));
                }
            } else {
                view.setEnabled(true);
            }
        }
    }




}
