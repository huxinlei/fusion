/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

/**
 * 处理逻辑类
 * <p>
 * Created by zhanglei_js on 2017/2/6.
 */
@FunctionalInterface
public interface Processor {

    /**
     * 自定义的tick逻辑
     */
    void process();

}
