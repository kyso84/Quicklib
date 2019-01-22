package com.quicklib.android.core;

import com.quicklib.android.core.helper.JavaHelper;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class JavaHelperTest {


    final Map<Integer, String> map1 = new HashMap<>();
    final Map<Integer, String> map2 = new HashMap<>();

    @BeforeAll
    public void setUp() throws Exception {

    }

    @Test
    public void getRandomInteger() throws Exception {
        final int checkIterations = 1000;
        final int min = 12;
        final int max = 15;

        int result;
        for(int x=0; x<checkIterations; x++){
            result = JavaHelper.getRandomInteger(min, max);
            assertThat(result).isNotNull();
            assertThat(result).isBetween(min, max);
        }
    }


    @Test
    public void getSetIntersec() throws Exception {
        // MAP #1
        map1.put(1, "one");
        map1.put(2, "two");
        // map1.put(3, "three");
        map1.put(4, "four");
        map1.put(5, "five");

        // MAP #2
        // map2.put(1, "another one");
        map2.put(2, "another two");
        // map2.put(3, "another three");
        // map2.put(4, "another four");
        map2.put(5, "another five");


        Set<Integer> result = JavaHelper.getSetIntersec(map1.keySet(), map2.keySet());

        assertThat(result).hasSize(2);
        assertThat(result).contains(2,5);
    }



    @Test
    public void getMapIntersec() throws Exception {
        // MAP #1
        map1.put(1, "one");
        map1.put(2, "two");
        // map1.put(3, "three");
        map1.put(4, "four");
        map1.put(5, "five");

        // MAP #2
        // map2.put(1, "another one");
        map2.put(2, "another two");
        // map2.put(3, "another three");
        // map2.put(4, "another four");
        map2.put(5, "another five");

        Map<Integer, String> result = JavaHelper.getMapIntersec(map1, map2);

        assertThat(result).hasSize(2);
        assertThat(result).doesNotContainKey(1);
        assertThat(result).containsKey(2);
        assertThat(result).doesNotContainKey(3);
        assertThat(result).doesNotContainKey(4);
        assertThat(result).containsKey(5);
        assertThat(result.get(2)).isEqualTo("two"); // by default we keep values from map1
        assertThat(result.get(5)).isEqualTo("five"); // by default we keep values from map1
    }

    @Test
    public void getMapIntersec_WithMap2Values() throws Exception {
        // MAP #1
        map1.put(1, "one");
        map1.put(2, "two");
        // map1.put(3, "three");
        map1.put(4, "four");
        map1.put(5, "five");

        // MAP #2
        // map2.put(1, "another one");
        map2.put(2, "another two");
        // map2.put(3, "another three");
        // map2.put(4, "another four");
        map2.put(5, "another five");

        Map<Integer, String> result = JavaHelper.getMapIntersec(map1, map2, true);

        assertThat(result).hasSize(2);
        assertThat(result).doesNotContainKey(1);
        assertThat(result).containsKey(2);
        assertThat(result).doesNotContainKey(3);
        assertThat(result).doesNotContainKey(4);
        assertThat(result).containsKey(5);
        assertThat(result.get(2)).isEqualTo("another two");
        assertThat(result.get(5)).isEqualTo("another five");
    }

    @Test
    public void getMapIntersec_WithNullMap() throws Exception {
        Map<Integer, String> result = JavaHelper.getMapIntersec(null, null);

        assertThat(result).hasSize(0);
    }


    @Test
    public void getMapIntersec_WithNoCommonValues() throws Exception {
        // MAP #1
        map1.put(1, "one");
        // map1.put(2, "two");
        // map1.put(3, "three");
        map1.put(4, "four");
        // map1.put(5, "five");

        // MAP #2
        // map2.put(1, "another one");
        map2.put(2, "another two");
        // map2.put(3, "another three");
        // map2.put(4, "another four");
        map2.put(5, "another five");

        Map<Integer, String> result = JavaHelper.getMapIntersec(map1, map2);

        assertThat(result).hasSize(0);
    }

    @Test
    public void getMapReverseIntersec() throws Exception {
        // MAP #1
        map1.put(1, "one");
        map1.put(2, "two");
        // map1.put(3, "three");
        // map1.put(4, "four");
        map1.put(5, "five");

        // MAP #2
        // map2.put(1, "another one");
        map2.put(2, "another two");
        // map2.put(3, "another three");
        map2.put(4, "another four");
        map2.put(5, "another five");

        Map<Integer, String> result = JavaHelper.getMapReverseIntersec(map1, map2);

        assertThat(result).hasSize(2);
        assertThat(result).containsKey(1);
        assertThat(result).doesNotContainKey(2);
        assertThat(result).doesNotContainKey(3);
        assertThat(result).containsKey(4);
        assertThat(result).doesNotContainKey(5);
        assertThat(result.get(1)).isEqualTo("one");
        assertThat(result.get(4)).isEqualTo("another four");
    }

}