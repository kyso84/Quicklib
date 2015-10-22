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
package com.quicklib.android.tool;

import android.util.Log;

import com.quicklib.android.BuildConfig;

import com.quicklib.android.core.Const;


/**
 * This class wrap Android's Log
 *
 * @author Benoit Deschanel
 * @since 15-04-30
 * Copyright (C) 2015 Quicklib
 */
public class Logger {

    private static boolean debug = false;

    public static String getStackTraceString(Throwable tr){
        return Log.getStackTraceString(tr);
    }
    public static boolean isLoggable(String tag, int level){
        return Log.isLoggable(tag, level);
    }
    public static int println(int priority, String tag, String msg){
        return Log.println(priority, tag, msg);
    }
    public static boolean isDebug(){
        return debug;
    }

    public static void setDebug(boolean debug){
        Logger.debug = debug;
    }

    // DEBUG METHODS
    public static int d(Throwable tr){
        return d(tr.getMessage(), tr);
    }

    public static int d(String msg){
        return d(Const.DEFAULT_TAG, msg);
    }
    public static int d(String msg, Throwable tr){
        return d(Const.DEFAULT_TAG, msg, tr);
    }
    public static int d(String tag, String msg){
        if(isDebug()){
            return Log.d(tag, msg);
        }
        return 0;
    }
    public static int d(String tag, String msg, Throwable tr){
        if(isDebug()){
            return Log.d(tag, msg, tr);
        }
        return 0;
    }

    // ERROR METHODS
    public static int e(Throwable tr){
        return e(tr.getMessage(), tr);
    }

    public static int e(String msg){
        return e(Const.DEFAULT_TAG, msg);
    }

    public static int e(String msg, Throwable tr){
        return e(Const.DEFAULT_TAG, msg, tr);
    }
    public static int e(String tag, String msg){
        if(isDebug()){
            return Log.e(tag, msg);
        }
        return 0;
    }
    public static int e(String tag, String msg, Throwable tr){
        if(BuildConfig.DEBUG){
            return Log.e(tag, msg, tr);
        }
        return 0;
    }

    // INFO METHODS
    public static int i(Throwable tr){
        return i(tr.getMessage(), tr);
    }

    public static int i(String msg){
        return i(Const.DEFAULT_TAG, msg);
    }

    public static int i(String msg, Throwable tr){
        return i(Const.DEFAULT_TAG, msg, tr);
    }
    public static int i(String tag, String msg){
        if(isDebug()){
            return Log.i(tag, msg);
        }
        return 0;
    }
    public static int i(String tag, String msg, Throwable tr){
        if(isDebug()){
            return Log.i(tag, msg, tr);
        }
        return 0;
    }

    // VERBOSE METHODS
    public static int v(Throwable tr){
        return v(tr.getMessage(), tr);
    }

    public static int v(String msg){
        return v(Const.DEFAULT_TAG, msg);
    }

    public static int v(String msg, Throwable tr){
        return v(Const.DEFAULT_TAG, msg, tr);
    }
    public static int v(String tag, String msg, Throwable tr){
        if(isDebug()){
            return Log.v(tag, msg, tr);
        }
        return 0;
    }
    public static int v(String tag, String msg){
        if(isDebug()){
            return Log.v(tag, msg);
        }
        return 0;
    }

    // WARNING METHODS
    public static int w(Throwable tr){
        return w(tr.getMessage(), tr);
    }

    public static int w(  String msg){
        return w(Const.DEFAULT_TAG, msg);
    }
    public static int w(String msg, Throwable tr){
        return w(Const.DEFAULT_TAG, msg, tr);
    }
    public static int w(String tag, String msg){
        if(isDebug()){
            return Log.w(tag, msg);
        }
        return 0;
    }
    public static int w(String tag, String msg, Throwable tr){
        if(isDebug()){
            return Log.w(tag, msg, tr);
        }
        return 0;
    }

    // WHAT A TERRIBLE FAILURE METHODS (or "What's The Fuck" Methods)
    public static int wtf(Throwable tr){
        return wtf(tr.getMessage(), tr);
    }

    public static int wtf(  String msg){
        return wtf(Const.DEFAULT_TAG, msg);
    }
    public static int wtf(String msg, Throwable tr){
        return wtf(Const.DEFAULT_TAG, msg, tr);
    }
    public static int wtf(String tag, String msg){
        if(isDebug()){
            return Log.wtf(tag, msg);
        }
        return 0;
    }
    public static int wtf(String tag, String msg, Throwable tr){
        if(isDebug()){
            return Log.wtf(tag, msg, tr);
        }
        return 0;
    }
}
