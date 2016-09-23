package com.quicklib.android.core;


import android.content.pm.PackageManager;

/**
 * @author bdescha1
 * @since 16-06-17
 * Copyright (C) 2016 French Connection !!!
 */
public class PermissionHelper {
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