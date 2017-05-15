/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

/**
 * 处理逻辑工厂类
 * <p>
 * Created by zhanglei_js on 2017/2/6.
 */
@FunctionalInterface
public interface ProcessorFactory {

    /**
     * 构建处理逻辑
     *
     * @return 处理逻辑
     */
    Processor newProcessor();

}
