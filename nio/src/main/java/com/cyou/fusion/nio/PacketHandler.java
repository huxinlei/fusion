/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

import com.cyou.fusion.nio.codec.Packet;

/**
 * 消息包处理接口
 * <p>
 * Created by zhanglei_js on 2017/5/8.
 */
public interface PacketHandler {

    /**
     * 消息处理
     *
     * @param session 会话
     */
    void handle(Session session, Packet packet);
}
