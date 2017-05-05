/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio.codec;

import com.cyou.fusion.nio.annotation.Packet;

/**
 * 消息包处理器
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
public enum PacketFactory {

    /**
     * 枚举单例
     */
    INSTANCE;

    /**
     * 消息包处理类
     */
    private Class[] classes = new Class[10000];

    /**
     * 注册消息包处理类
     */
    public void register(Class<? extends AbstractPacket> cls) {
        classes[cls.getAnnotation(Packet.class).value()] = cls;
    }

    /**
     * 生成消息包
     *
     * @param packetID 消息ID
     * @return 消息包
     */
    public AbstractPacket newPacket(short packetID) {
        try {
            return (AbstractPacket) classes[packetID].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

}
