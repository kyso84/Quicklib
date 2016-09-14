package com.quicklib.android.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This bring new features to standard QuickSharedPreferences
 *
 * @author Benoit Deschanel
 * Copyright (C) 2015 Quicklib
 */
public class QuickSharedPreferences implements SharedPreferences {

    private static final Gson gson = new Gson();


    private android.content.SharedPreferences sharedPreferences;

    public QuickSharedPreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public QuickSharedPreferences(Context context, String prefName) {
        sharedPreferences = context.getSharedPreferences(prefName, 0);
    }


    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }


    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }


    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return null;
    }


    @Override
    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }


    @Override
    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }


    @Override
    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }


    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }


    @Override
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }


    @Override
    public Editor edit() {
        return new Editor(sharedPreferences.edit());
    }


    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }


    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }


    public String[] getStringArray(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), String[].class );
        } catch (JsonSyntaxException e) {
            return new String[0];
        }
    }

    public List<String> getStringList(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), new TypeToken<List<String>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return Collections.emptyList();
        }
    }

    public boolean[] getBooleanArray(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"),  boolean[].class);
        } catch (JsonSyntaxException e) {
            return new boolean[0];
        }
    }

    public List<Boolean> getBooleanList(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), new TypeToken<List<Boolean>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return Collections.emptyList();
        }
    }

    public int[] getIntArray(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), int[].class);
        } catch (JsonSyntaxException e) {
            return new int[0];
        }
    }

    public List<Integer> getIntList(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), new TypeToken<List<Integer>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return Collections.emptyList();
        }
    }


    public float[] getFloatArray(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), float[].class);
        } catch (JsonSyntaxException e) {
            return new float[0];
        }
    }

    public List<Float> getFloatList(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), new TypeToken<List<Float>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return Collections.emptyList();
        }
    }

    public long[] getLongArray(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), long[].class);
        } catch (JsonSyntaxException e) {
            return new long[0];
        }
    }

    public List<Long> getLongList(String key) {
        try {
            return gson.fromJson(sharedPreferences.getString(key, "[]"), new TypeToken<List<Long>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            return Collections.emptyList();
        }
    }


    public static class Editor implements android.content.SharedPreferences.Editor {

        private android.content.SharedPreferences.Editor editor;

        private Editor(android.content.SharedPreferences.Editor editor) {
            this.editor = editor;
        }

        @Override
        public Editor putString(String key, String value) {
            editor.putString(key, value);
            return this;
        }


        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Editor putStringSet(String key, Set<String> values) {
            editor.putStringSet(key, values);
            return this;
        }


        @Override
        public Editor putInt(String key, int value) {
            editor.putInt(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            editor.putLong(key, value);
            return this;
        }


        @Override
        public Editor putFloat(String key, float value) {
            editor.putFloat(key, value);
            return this;
        }


        @Override
        public Editor putBoolean(String key, boolean value) {
            editor.putBoolean(key, value);
            return this;
        }


        public Editor putStringArray(String key, String[] value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putStringList(String key, List<String> value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putBooleanArray(String key, boolean[] value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putBooleanList(String key, List<Boolean> value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putIntArray(String key, int[] value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putIntList(String key, List<Integer> value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }


        public Editor putFloatArray(String key, float[] value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putFloatList(String key, List<Float> value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putLongArray(String key, long[] value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        public Editor putLongList(String key, List<Long> value) {
            editor.putString(key, gson.toJson(value));
            return this;
        }

        @Override
        public Editor remove(String key) {
            editor.clear();
            return this;
        }


        @Override
        public Editor clear() {
            editor.clear();
            return this;
        }


        @Override
        public boolean commit() {
            return editor.commit();
        }


        @Override
        public void apply() {
            editor.apply();
        }
    }


}
