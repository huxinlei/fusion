/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.core.evnet;

/**
 * 事件循环类
 * <p>
 * Created by zhanglei_js on 2017/5/11.
 */
public class EventLooper {

    /**
     * 本地对象
     */
    private static ThreadLocal<EventLooper> sThreadLocal = new ThreadLocal<>();

    /**
     * 绑定的线程ID
     */
    long mThreadId;

    /**
     * 事件队列
     */
    EventQueue mQueue;

    /**
     * 构造函数
     */
    private EventLooper() {
        mQueue = new EventQueue();
        mThreadId = Thread.currentThread().getId();
    }

    /**
     * 初始化
     */
    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new EventLooper());
    }

    /**
     * 获取本地事件循环对象
     *
     * @return 事件循环对象 event looper
     */
    static EventLooper myLooper() {
        return sThreadLocal.get();
    }

    /**
     * 处理队列内所有事件
     */
    public static void execute() {
        final EventLooper me = myLooper();

        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final EventQueue queue = me.mQueue;

        // 遍历所有Event
        Event event;
        while ((event = queue.pop()) != null) {
            // 执行Event 或 执行回调
            event.target.dispatchMessage(event);
        }
    }

}
