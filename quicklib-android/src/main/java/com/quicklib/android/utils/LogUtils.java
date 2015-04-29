package com.quicklib.android.utils;

import android.util.Log;

import com.kysoprod.framework.BuildConfig;

public class LogUtils {

    private static final String DEFAULT_TAG = "YPG";
    private static boolean debug = BuildConfig.DEBUG;

    public static String getStackTraceString(Throwable tr){
        return Log.getStackTraceString(tr);
    }
    public static boolean isLoggable(String tag, int level){
        return Log.isLoggable(tag, level);
    }
    public static int println(int priority, String tag, String msg){
        return Log.println(priority, tag, msg);
    }

    public static void setDebug(boolean active){
        debug = active;
    }

    public static boolean isDebug(){
        return debug;
    }

    // DEBUG METHODS
    public static int d(String msg){
        return d(DEFAULT_TAG, msg);
    }
    public static int d(Throwable tr){
        return d(DEFAULT_TAG, tr.getMessage(), tr);
    }
    public static int d(String msg, Throwable tr){
        return d(DEFAULT_TAG, msg, tr);
    }
    public static int d(String tag, String msg){
        if(debug){
            return Log.d(tag, msg);
        }
        return 0;
    }
    public static int d(String tag, String msg, Throwable tr){
        if(debug){
            return Log.d(tag, msg, tr);
        }
        return 0;
    }

    // ERROR METHODS
    public static int e(String msg){
        return e(DEFAULT_TAG, msg);
    }
    public static int e(Throwable tr){
        return e(DEFAULT_TAG, tr.getMessage(), tr);
    }
    public static int e(String msg, Throwable tr){
        return e(DEFAULT_TAG, msg, tr);
    }
    public static int e(String tag, String msg){
        if(debug){
            return LogUtils.e(tag, msg);
        }
        return 0;
    }
    public static int e(String tag, String msg, Throwable tr){
        if(debug){
            return LogUtils.e(tag, msg, tr);
        }
        return 0;
    }

    // INFO METHODS
    public static int i(String msg){
        return i(DEFAULT_TAG, msg);
    }
    public static int i(Throwable tr){
        return i(DEFAULT_TAG, tr.getMessage(), tr);
    }
    public static int i(String msg, Throwable tr){
        return i(DEFAULT_TAG, msg, tr);
    }
    public static int i(String tag, String msg){
        if(debug){
            return LogUtils.e(tag, msg);
        }
        return 0;
    }
    public static int i(String tag, String msg, Throwable tr){
        if(debug){
            return Log.i(tag, msg, tr);
        }
        return 0;
    }

    // VERBOSE METHODS
    public static int v(String msg){
        return v(DEFAULT_TAG, msg);
    }
    public static int v(Throwable tr){
        return v(DEFAULT_TAG, tr.getMessage(), tr);
    }
    public static int v(String msg, Throwable tr){
        return v(DEFAULT_TAG, msg, tr);
    }
    public static int v(String tag, String msg, Throwable tr){
        if(debug){
            return Log.v(tag, msg, tr);
        }
        return 0;
    }
    public static int v(String tag, String msg){
        if(debug){
            return Log.v(tag, msg);
        }
        return 0;
    }

    // WARNING METHODS
    public static int w(  String msg){
        return w(DEFAULT_TAG, msg);
    }
    public static int w(Throwable tr){
        return w(DEFAULT_TAG, tr.getMessage(), tr);
    }
    public static int w(String msg, Throwable tr){
        return w(DEFAULT_TAG, msg, tr);
    }
    public static int w(String tag, String msg){
        if(debug){
            return Log.w(tag, msg);
        }
        return 0;
    }
    public static int w(String tag, String msg, Throwable tr){
        if(debug){
            return Log.w(tag, msg, tr);
        }
        return 0;
    }

    // WHAT A TERRIBLE FAILURE METHODS (or "What The Fuck" Methods)
    public static int wtf(  String msg){
        return wtf(DEFAULT_TAG, msg);
    }
    public static int wtf(Throwable tr){
        return e(DEFAULT_TAG, tr);
    }
    public static int wtf(String msg, Throwable tr){
        return wtf(DEFAULT_TAG, msg, tr);
    }
    public static int wtf(String tag, String msg){
        if(debug){
            return Log.wtf(tag, msg);
        }
        return 0;
    }
    public static int wtf(String tag, String msg, Throwable tr){
        if(debug){
            return Log.wtf(tag, msg, tr);
        }
        return 0;
    }
}
