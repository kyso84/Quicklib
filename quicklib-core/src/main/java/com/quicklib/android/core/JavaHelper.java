package com.quicklib.android.core;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author bdescha1
 * @since 16-07-27
 * Copyright (C) 2016 French Connection !!!
 */
public class JavaHelper {

    public static int getRandomInteger(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    public static void delayAction(long delay, Runnable runnable) {
        new Handler().postDelayed(runnable, delay);
    }

    private static <T, E> Map<T, E> getMapIntersec(Map<T, E> mapOne, Map<T, E> mapTwo) {
        return getMapIntersec(mapOne, mapTwo.keySet());
    }

    private static <T, E> Map<T, E> getMapIntersec(Map<T, E> mapOne, Set<T> keyList) {
        Map<T, E> result = new HashMap<>();
        for (T key : mapOne.keySet()) {
            if (keyList.contains(key)) {
                result.put(key, mapOne.get(key));
            }
        }
        return result;
    }

    private static <T, E> Map<T, E> getMapReverseIntersec(Map<T, E> mapOne, Map<T, E> mapTwo) {
        return getMapReverseIntersec(mapOne, mapTwo.keySet());
    }

    private static <T, E> Map<T, E> getMapReverseIntersec(Map<T, E> mapOne, Set<T> keyList) {
        Map<T, E> result = new HashMap<>();
        for (T key : mapOne.keySet()) {
            if (!keyList.contains(key)) {
                result.put(key, mapOne.get(key));
            }
        }
        return result;
    }


}
