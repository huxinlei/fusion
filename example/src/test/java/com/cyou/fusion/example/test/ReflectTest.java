package com.cyou.fusion.example.test;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试反射效率
 * <p>
 * 测试结果：
 * 正常生成对象耗时:6
 * 反射生成对象耗时:299
 * 正常调用方法耗时:7
 * 反射效用方法耗时:217
 * </p>
 * Created by zhanglei_js on 2017/5/8.
 */
public class ReflectTest {

    /**
     * 循环一亿次
     */
    private static int LOOP_COUNT = 100_000_000;

    /**
     * 测试反射和正常生成耗时
     */
    @Test
    public void testNewAndNewInstance() throws IllegalAccessException, InstantiationException {
        // 正常生成对象
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            new Object();
        }
        System.out.println("正常生成对象耗时:" + (System.currentTimeMillis() - start));

        // 反射生成对象
        start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            Object.class.newInstance();
        }
        System.out.println("反射生成对象耗时:" + (System.currentTimeMillis() - start));
    }

    /**
     * 测试反射和正常生成耗时
     */
    @Test
    public void testCallAndInvoke() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // 正常调用方法
        Object object = new Object();
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            object.getClass();
        }
        System.out.println("正常调用方法耗时:" + (System.currentTimeMillis() - start));

        // 反射调用方法
        Method method = Object.class.getMethod("getClass");
        start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            method.invoke(object);
        }
        System.out.println("反射效用方法耗时:" + (System.currentTimeMillis() - start));
    }


}
