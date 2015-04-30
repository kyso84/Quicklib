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
package com.quicklib.android.android;

import android.app.Application;
import android.content.Context;


/**
 * This class overload Android's Application and provides useful features
 *
 * @author Benoit Deschanel
 * @since 15-04-30
 * Copyright (C) 2015 Quicklib
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
