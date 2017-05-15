package com.cyou.fusion.nio;

import io.netty.channel.Channel;

/**
 * 线程选择器
 * Created by zhanglei_js on 2017/5/8.
 */
@FunctionalInterface
public interface Selector {

    /**
     * 线程选择策略
     *
     * @param ticks   线程组
     * @param channel 客户端连接
     * @return 选定的线程
     */
    Tick select(Tick[] ticks, Channel channel);
}
