package com.quicklib.android.core;

import android.content.pm.PackageManager;

import com.quicklib.android.core.helper.PermissionHelper;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PermissionHelperTest {
    @Test
    public void testCheckPermissionsGranted() throws Exception {
        int[] input = new int[]{PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED};

        boolean result = PermissionHelper.onRequestPermissionsResult(input);

        assertThat(result).isTrue();
    }

    @Test
    public void testCheckPermissionsGranted_IfOneDenied() throws Exception {
        int[] input = new int[]{PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED};

        boolean result = PermissionHelper.onRequestPermissionsResult(input);

        assertThat(result).isFalse();
    }

}