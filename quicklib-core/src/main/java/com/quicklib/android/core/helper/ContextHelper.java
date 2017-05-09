package com.quicklib.android.core.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.DimenRes;
import android.support.annotation.ColorInt;
import android.support.annotation.AttrRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.util.TypedValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This helper provides convenient methods related to the app context
 *
 * @author Benoit Deschanel
 * @package com.quicklib.android.core.helper
 * @since 17-05-02
 */
public class ContextHelper {

    /**
     * Gets a density independent dimension.
     *
     * @param context the app context
     * @param dimId   the dimension id
     * @return the density independent dimension
     */
    public static float getDimFloat(Context context, @DimenRes int dimId) {
        return context.getResources().getDimension(dimId);
    }
    
    /**
     * Gets the amount of pixels of a dimension.
     *
     * @param context the app context
     * @param dimenId   the dimension id
     * @return the number of pixels
     */
    @ColorInt
    public static int getDimenPx(Context context, @DimenRes int dimenId) {
        return context.getResources().getDimensionPixelSize(dimenId);
    }

    /**
     * Gets a color defined in the theme.
     *
     * @param context the app context
     * @param attrRes   the attribute id (e.g. R.attr.colorAccent)
     * @return the density independent dimension
     */
    @ColorInt
    public static int getThemeColor(final Context context, @AttrRes int attrRes) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attrRes, value, true);
        return value.data;
    }

    /**
     * Show soft keyboard.
     *
     * @param view the attached view
     */
    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }

    /**
     * Hide soft keyboard.
     *
     * @param view the attached view
     */
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hide soft keyboard.
     *
     * @param activity the activity
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Gets version name of the app.
     *
     * @param context the app context
     * @return the version name
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName == null ? "" : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * Gets version code of the app.
     *
     * @param context the app context
     * @return the version code
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }

    /**
     * check if an application is installed on the device.
     *
     * @param context     the context
     * @param packageName the package name to check
     * @return the installation status
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Read a file (classic).
     *
     * @param context the app context
     * @param file    the file to read
     * @return the file content
     */
    public static String readFile(Context context, File file) {
        InputStream is = null;
        try {
            is = context.openFileInput(file.getAbsolutePath());
            return StringHelper.fromStream(is);
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return "";
    }

    /**
     * Read a file (classic).
     *
     * @param context the app context
     * @param filePath   path to the file to read
     * @return the file content
     */
    public static String readFile(Context context, String filePath) {
        InputStream is = null;
        try {
            is = context.openFileInput(filePath);
            return StringHelper.fromStream(is);
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return "";
    }

    /**
     * Read a file from asset directory.
     *
     * @param context  the app context
     * @param filePath the filename in the asset directory (relative path)
     * @return the file content
     */
    public static String readAssetFile(Context context, String filePath) {
        InputStream is = null;
        try {
            is = context.getAssets().open(filePath);
            return StringHelper.fromStream(is);
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return "";
    }

    /**
     * Check if network is available.
     *
     * @param context the app context
     * @return the network status
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
