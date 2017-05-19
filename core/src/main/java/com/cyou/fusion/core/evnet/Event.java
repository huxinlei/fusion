/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.core.evnet;

/**
 * 线程间通信的事件类
 * <p>
 * Created by zhanglei_js on 2017/5/11.
 */
public class Event<T> implements Cloneable {

    /**
     * 处理器对象
     */
    EventHandler target;

    /**
     * 回调对象
     */
    EventCallback<T> eventCallback;

    /**
     * 回调对象
     */
    @Deprecated
    Runnable callback;

    /**
     * 回调源
     */
    long src;

    /**
     * 消息ID
     */
    private int what;

    /**
     * 消息内容
     */
    private Object data;

    /**
     * 构造函数(回调)
     */
    Event() {
    }

    /**
     * 构造函数(事件)
     *
     * @param what 消息ID
     * @param data 消息内容
     */
    public Event(int what, Object data) {
        this.what = what;
        this.data = data;
        src = Thread.currentThread().getId();
    }

    /**
     * 构造函数(事件+回调)
     *
     * @param what 消息ID
     * @param data 消息内容
     */
    public Event(int what, Object data, EventCallback<T> eventCallback) {
        this.what = what;
        this.data = data;
        this.eventCallback = eventCallback;
        src = Thread.currentThread().getId();
    }

    /**
     * Gets what.
     *
     * @return the what
     */
    public int getWhat() {
        return what;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }


    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Can't clone the event object.", e);
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "data=" + data +
                ", what=" + what +
                '}';
    }
}
