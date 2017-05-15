/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.core.evnet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 事件处理类
 * <p>
 * Created by zhanglei_js on 2017/5/11.
 */
public abstract class EventHandler {

    /**
     * 消息队列
     */
    private EventQueue mQueue;


    /**
     * 绑定的线程ID
     */
    private long mThreadId;

    /**
     * 标记组
     */
    private List<Object> mTags;

    /**
     * 构造函数
     */
    protected EventHandler() {
        /**
         * 获取当前线程的时间循环对象
         */
        EventLooper mLooper = EventLooper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException("Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
        mThreadId = mLooper.mThreadId;

        mTags = new CopyOnWriteArrayList<>();
    }

    /**
     * 分发事件
     *
     * @param event 事件对象
     */
    void dispatchMessage(Event event) {
        if (event.callback != null) {
            // 处理回调
            handleCallback(event);
        } else {
            // 处理事件
            handleMessage(event);
        }
    }

    /**
     * 自定义事件的处理
     *
     * @param event 事件
     */
    public abstract void handleMessage(Event event);

    /**
     * 自定义回调处理
     *
     * @param event 事件
     */
    private static void handleCallback(Event event) {
        event.callback.run();
    }

    /**
     * 向事件队列中插入一条事件/回调
     *
     * @param event 事件
     */
    void post(Event event) {
        event.target = this;
        mQueue.push(event);
    }

    /**
     * 向事件队列中插入一条回调
     *
     * @param runnable 事件
     */
    void post(Runnable runnable) {
        Event event = new Event();
        event.callback = runnable;
        post(event);
    }

    /**
     * Gets thread id.
     *
     * @return the thread id
     */
    public long getThreadId() {
        return mThreadId;
    }

    /**
     * 增加一个标记
     *
     * @param flag 标记
     */
    public void addTags(Object flag) {
        mTags.add(flag);
    }

    /**
     * 删除一个标记
     *
     * @param flag 标记
     */
    public void removeTags(Object flag) {
        mTags.remove(flag);
    }


    /**
     * Gets tags.
     *
     * @return the tags
     */
    public List<Object> getTags() {
        return mTags;
    }
}
