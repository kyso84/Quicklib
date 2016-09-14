package com.quicklib.android.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ypg.find.core.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import timber.log.Timber;

/**
 * @author bdescha1
 * @since 16-06-17
 * Copyright (C) 2016 French Connection !!!
 */
public class ContextHelper {

    public static
    @ColorInt
    int getColor(Context context, @ColorRes int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    public static float getDimFloat(Context context, @DimenRes int dimId) {
        return context.getResources().getDimension(dimId);
    }

    public static int getDimenPx(@DimenRes int dimenId) {
        return App.get().getResources().getDimensionPixelSize(dimenId);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String getVersionName(Context conext) {
        PackageManager pm = conext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(conext.getPackageName(), 0);
            return pi.versionName == null ? "" : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String readAssetFile(Context context, String filePath) {
        InputStream is = null;
        BufferedReader in = null;



        StringBuilder result = new StringBuilder();
        InputStream is = null;
        BufferedReader in = null;
        try {
            is = context.getAssets().open(filePath);
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));


            is = context.getAssets().open(filePath);
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            return StringUtils.fromStream()



        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return result.toString();
    }




}