package com.quicklib.android.multidex;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public abstract class QuickAppMultidex extends MultiDexApplication {

    private static QuickAppMultidex me;

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
    }

    public static QuickAppMultidex getApplication() {
        return me;
    }

    public static Context getContext() {
        return me.getApplicationContext();
    }

}
