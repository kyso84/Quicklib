package com.quicklib.android.core.helper;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * This class helps to work with permissions
 *
 * @author Benoit Deschanel
 * @since 16-09-26
 * Copyright (C) 2016 Quicklib
 */
public class PermissionHelper {

    /**
     * Check permissions callback's result
     *
     * @param grantResults list of permissions results
     * @return returns if all requested permissions are granted
     */
    public static boolean onRequestPermissionsResult(int[] grantResults) {
        if (grantResults != null) {
            boolean result = true;
            for (int status : grantResults) {
                result = result && (status == PackageManager.PERMISSION_GRANTED);
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * Check permissions status
     *
     * @param permissionAsked list of permissions needed
     * @return returns if all needed permissions are granted
     */
    public static boolean checkSelfPermission(Context context  , String... permissionAsked) {
        if (permissionAsked != null) {
            boolean result = true;
            for (String onePermission : permissionAsked) {
                result = result && (ActivityCompat.checkSelfPermission(context, onePermission) == PackageManager.PERMISSION_GRANTED);
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * Check permissions status
     *
     * @param permissionAsked list of permissions needed
     * @return returns if all needed permissions are granted
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity  , String... permissionAsked) {
        if (permissionAsked != null) {
            boolean result = true;
            for (String onePermission : permissionAsked) {
                result = result || ActivityCompat.shouldShowRequestPermissionRationale(activity, onePermission);
            }
            return result;
        } else {
            return false;
        }
    }
}