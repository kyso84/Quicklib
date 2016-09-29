package com.quicklib.android.core.tool;


public class Log {

    public interface Callback {
        void log(int priority, Throwable t, String message, Object... args);
    }

    private static Callback callback = null;
    private static boolean enable = false;

    public static void setCallback(Callback callback) {
        Log.callback = callback;
    }

    public static void v(String message, Object... args) {
        log(android.util.Log.VERBOSE, message, args);
    }

    public static void v(Throwable t, String message, Object... args) {
        log(android.util.Log.VERBOSE, t, message, args);
    }

    public static void d(String message, Object... args) {
        log(android.util.Log.DEBUG, message, args);
    }

    public static void d(Throwable t, String message, Object... args) {
        log(android.util.Log.DEBUG, t, message, args);
    }

    public static void i(String message, Object... args) {
        log(android.util.Log.INFO, message, args);
    }

    public static void i(Throwable t, String message, Object... args) {
        log(android.util.Log.INFO, t, message, args);
    }

    public static void w(String message, Object... args) {
        log(android.util.Log.WARN, message, args);
    }

    public static void w(Throwable t, String message, Object... args) {
        log(android.util.Log.WARN, t, message, args);
    }

    public static void e(String message, Object... args) {
        log(android.util.Log.ERROR, message, args);
    }

    public static void e(Throwable t, String message, Object... args) {
        log(android.util.Log.ERROR, t, message, args);
    }

    public static void wtf(String message, Object... args) {
        log(android.util.Log.ERROR, message, args);
    }

    public static void wtf(Throwable t, String message, Object... args) {
        log(android.util.Log.ERROR, t, message, args);
    }

    public static void log(int priority, String message, Object... args) {
        log(priority, null, message, args);
    }

    public static void log(int priority, Throwable t, String message, Object... args) {
        if (enable) {
            android.util.Log.println(priority, "Quicklib", message);
        }
        if (callback != null) {
            callback.log(priority, t, message, args);
        }
    }


}
