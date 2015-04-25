package com.quicklib.android.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by bdescha1 on 15-03-17.
 */
public class App extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
