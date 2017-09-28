package com.quicklib.android.core.helper;

import android.os.Handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This class helps to work with "pure" java object (without 3rd party lib)
 *
 * @author Benoit Deschanel
 * @since 16-09-26
 * Copyright (C) 2016 Quicklib
 */
public class JavaHelper {

    private static final Random r = new Random();

    /**
     * Generate a value between min and max
     *
     * @param min
     * @param max
     * @return a random value
     */
    public static int getRandomInteger(int min, int max) {
        return r.nextInt(max - min) + min;
    }

    public static void delayAction(long delay, Runnable runnable) {
        if( delay > 0 ){
            new Handler().postDelayed(runnable, delay);
        }else{
            runnable.run();
        }
    }


    public static <T> Set<T> getSetIntersec(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>();
        for (T value : set1) {
            if (set2.contains(value)) {
                result.add(value);
            }
        }
        return result;
    }

    public static <T, E> Map<T, E> getMapIntersec(Map<T, E> map1, Map<T, E> map2) {
        return getMapIntersec(map1, map2, false);
    }

    public static <T, E> Map<T, E> getMapIntersec(Map<T, E> map1, Map<T, E> map2, boolean map2Values) {
        Map<T, E> result = new HashMap<>();
        if (map1 != null && map2 != null) {
            for (Map.Entry<T, E> entry1 : map1.entrySet()) {
                T key = entry1.getKey();
                if (map2.containsKey(key)) {
                    if (map2Values) {
                        result.put(key, map2.get(key));
                    } else {
                        result.put(key, map1.get(key));
                    }
                }
            }
        }
        return result;
    }

    public static <T, E> Map<T, E> getMapReverseIntersec(Map<T, E> map1, Map<T, E> map2) {
        Map<T, E> result = new HashMap<>();
        if (map1 != null && map2 != null) {
            for (Map.Entry<T, E> entry1 : map1.entrySet()) {
                T key = entry1.getKey();
                if (!map2.containsKey(key)) {
                    result.put(key, map1.get(key));
                }
            }
            for (Map.Entry<T, E> entry2 : map2.entrySet()) {
                T key = entry2.getKey();
                if (!map1.containsKey(key)) {
                    result.put(key, map2.get(key));
                }
            }
        }
        return result;
    }

}
