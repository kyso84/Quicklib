package com.quicklib.android.helper;

import android.content.Context;
import android.support.annotation.StringRes;

import com.ypg.find.R;
import com.ypg.find.core.Const;

/**
 * @author bdescha1
 * @since 16-09-09
 * Copyright (C) 2016 French Connection !!!
 */
public class SecurityHelper {
    /*
        DAGGERS MODULES ARE NOT OBFUSCATED BY PROGUARD, SO I CREATED THIS 2 METHODS TO KEEP IT SECRET
        DONT WORRY, PROGUARD WILL REMOVE ALL COMMENTS :)

        Original key:   tn0w228F5Hv4Wej+
        Splitted key:   t   n   0   w   2   2   8   F   5   H   v   4   W   e   j   +
        Hex key:        74  6e  30  77  32  32  38  46  35  48  76  34  57  65  6a  2B
        Dec key:        116 110 48  119 50  50  56  70  53  72  118 52  87  101 106 43
        Parts:          |________P1_______|_____P2____|_____P3_____|________P4________|

        Source: https://www.easycalculation.com/ascii-hex.php
    */
    public static String getSharedKey(Context context, @StringRes int sharedkeyId) {
        return context.getString(sharedkeyId);

    }

    public static String getIV(Context context) {
        int[] part1 = context.getResources().getIntArray(R.array.sizes);
        String part2 = "28F";
        String part3 = context.getResources().getString(R.string.prefix);
        int[] part4 = new int[]{Const.CODE_SERVER_1, Const.CODE_SERVER_2, Const.CODE_SERVER_3, Const.CODE_SERVER_4};

        StringBuilder sb = new StringBuilder();
        for (int code : part1) {
            sb.append((char) code);
        }
        sb.append(part2);
        sb.append(part3);
        for (int code : part4) {
            sb.append((char) code);
        }
        return sb.toString() + "+";
    }
}
