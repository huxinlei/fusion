/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

/**
 * 处理逻辑类
 * <p>
 * Created by zhanglei_js on 2017/2/6.
 */
public interface Processor {

    /**
     * 自定义的tick运行前逻辑
     */
    void prepare(Tick tick);

    /**
     * 自定义的tick结束前逻辑
     */
    void terminate(Tick tick);

    /**
     * 自定义的tick逻辑
     */
    void tick(Tick tick);

    /**
     * 每个session的tick逻辑
     */
    void tick(Tick tick, Session session);

    /**
     * 每个session的断线回调逻辑
     */
    void disconnect(Tick tick, Session session);


}
