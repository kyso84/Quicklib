/*
 * Copyright (C) 2015 Quicklib
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quicklib.android.utils;

import com.quicklib.android.tool.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides useful features to manipulate Strings
 *
 * @author Benoit Deschanel
 * @since 15-04-30
 * Copyright (C) 2015 Quicklib
 */
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
                Logger.e(e);
            } finally {
                try {
                    if( is != null ) {
                        is.close();
                    }
                } catch (IOException ignored) {
                    Logger.e(ignored);
                    ret = "";
                }
            }
        }else{
            ret = "";
        }
        return ret;
    }


}
