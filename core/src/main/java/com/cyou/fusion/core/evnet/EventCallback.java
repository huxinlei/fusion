/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.core.evnet;

/**
 * 事件回调
 * <p>
 * Created by zhanglei_js on 2017/5/19.
 */
public interface EventCallback<T> {

    void onSuccess(T t);

    void onFailure(Throwable ex);

}
