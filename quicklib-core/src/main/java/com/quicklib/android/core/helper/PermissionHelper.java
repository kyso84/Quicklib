package com.quicklib.android.core.helper;


import android.content.pm.PackageManager;

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
     * @return returns if all resquested permissions are granted
     */
    public static boolean checkPermissionsGranted(int[] grantResults) {
        if (grantResults != null) {
            for (int status : grantResults) {
                if (status != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}