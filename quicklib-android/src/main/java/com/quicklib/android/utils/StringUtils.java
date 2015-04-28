package com.quicklib.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StringUtils {

    public static final String EMPTY_STRING = "";

    public static String fromStream(InputStream is) {
        String ret = "";
        if (is != null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int count;
                do {
                    count = is.read(buffer);
                    if (count > 0) {
                        out.write(buffer, 0, count);
                    }
                } while (count >= 0);
                ret = out.toString();
            } catch (IOException e) {
                LogUtils.e(e);
            } finally {
                try {
                    if( is != null ) {
                        is.close();
                    }
                } catch (IOException ignored) {
                    LogUtils.e(ignored);
                    ret = "";
                }
            }
        }else{
            ret = "";
        }
        return ret;
    }


}
