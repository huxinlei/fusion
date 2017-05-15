/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.core.evnet;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 事件队列类
 * <p>
 * Created by zhanglei_js on 2017/5/11.
 */
class EventQueue {

    /**
     * 所有事件
     */
    private Queue<Event> events = new ConcurrentLinkedQueue<>();

    /**
     * 获取一个事件
     *
     * @return 事件
     */
    Event pop() {
        return events.poll();
    }

    /**
     * 插入一个事件
     *
     * @param event 事件
     * @return 插入结果
     */
    boolean push(Event event) {
        return events.add(event);
    }
}
