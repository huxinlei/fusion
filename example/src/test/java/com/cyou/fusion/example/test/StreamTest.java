package com.cyou.fusion.example.test;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Stream效率
 * <p>
 * 测试结果：
 * for max 结果:9999 耗时:41
 * foreach max 结果:9998 耗时:76
 * stream max 结果:9999 耗时:84
 * for loop 耗时:3
 * foreach loop 耗时:5
 * stream loop 耗时:5
 * </p>
 * Created by zhanglei_js on 2017/5/8.
 */
public class StreamTest {

    /**
     * 循环一亿次
     */
    private static int LOOP_COUNT = 100_000_000;
    private int[] array = new int[LOOP_COUNT];

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < LOOP_COUNT; i++) {
            array[i] = (int) (Math.random() * 10000);
        }
    }

    @Test
    public void testMax() throws IllegalAccessException, InstantiationException {
        // for max
        long start = System.currentTimeMillis();
        int maxFor = 0;
        for (int i = 0; i < LOOP_COUNT; i++) {
            if (maxFor < array[i]) {
                maxFor = array[i];
            }
        }
        System.out.println("for max 结果:" + maxFor + " 耗时:" + (System.currentTimeMillis() - start));

        // foreach max
        maxFor = 0;
        start = System.currentTimeMillis();
        for (int i : array) {
            if (maxFor < array[i]) {
                maxFor = array[i];
            }
        }
        System.out.println("foreach max 结果:" + maxFor + " 耗时:" + (System.currentTimeMillis() - start));

        // stream max
        start = System.currentTimeMillis();
        int maxStream = Arrays.stream(array).reduce((left, right) -> left > right ? left : right).orElse(0);
        System.out.println("stream max 结果:" + maxStream + " 耗时:" + (System.currentTimeMillis() - start));

    }

    @Test
    public void testLoop() throws IllegalAccessException, InstantiationException {
        // for loop
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {

        }
        System.out.println("for loop 耗时:" + (System.currentTimeMillis() - start));

        // foreach loop
        start = System.currentTimeMillis();
        for (int i : array) {

        }
        System.out.println("foreach loop 耗时:" + (System.currentTimeMillis() - start));

        // stream loop
        start = System.currentTimeMillis();
        Arrays.stream(array).forEach(num -> {
        });
        System.out.println("stream loop 耗时:" + (System.currentTimeMillis() - start));

    }


}
