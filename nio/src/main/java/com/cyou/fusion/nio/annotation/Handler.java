/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 协议包处理模块注解
 * <p>
 * Created by zhanglei_js on 2017/1/24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
    /**
     * 协议编号（PacketID）
     *
     * @return 协议编号
     */
    short value() default (short) -1;
}
