/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.core.evnet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * 事件总线
 * <p>
 * Created by zhanglei_js on 2017/5/11.
 */
public enum EventBus {

    /**
     * 单例
     */
    INSTANCE;

    /**
     * 已注册处理器集合
     */
    private static List<EventHandler> handlers = new CopyOnWriteArrayList<>();

    /**
     * 注册事件处理器
     *
     * @param eventHandler 事件处理器
     */
    public void registerEventHandler(EventHandler eventHandler) {
        handlers.add(eventHandler);
    }

    /**
     * 获取指定线程的处理器
     *
     * @param threadID 线程ID
     * @return 处理器
     */
    public EventHandler getHandler(long threadID) {
        return handlers.stream().filter(eventHandler -> eventHandler.getThreadId() == threadID).findAny().orElse(null);
    }

    /**
     * 广播事件给所有线程
     *
     * @param event 事件
     */
    public void broadcast(Event event) {
        handlers.stream().forEach(handler -> {
            Event clone = (Event) event.clone();
            handler.post(clone);
        });
    }

    /**
     * 广播事件给所有线程
     *
     * @param event     事件
     * @param predicate 过滤条件
     */
    public void broadcast(Event event, Predicate<EventHandler> predicate) {
        handlers.stream().filter(predicate).forEach(handler -> {
            Event clone = (Event) event.clone();
            handler.post(clone);
        });
    }

    /**
     * 回调给事件产生线程
     *
     * @param runnable 回调
     * @param event    事件
     */
    public void callback(Runnable runnable, Event event) {
        handlers.stream().filter(eventHandler -> eventHandler.getThreadId() == event.src).forEach(handler -> handler.post(runnable));
    }

}
