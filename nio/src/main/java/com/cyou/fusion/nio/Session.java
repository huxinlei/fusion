/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Session.
 */
public class Session {

    /**
     * 会话状态枚举
     */
    public enum STATUS {
        ESTABLISHED, LOGGED, DELETABLE
    }

    /**
     * 收到的消息包属性名
     */
    public static final String ATTR_INPUT = "INPUT";

    /**
     * 发送的消息包属性名
     */
    public static final String ATTR_OUTPUT = "OUTPUT";

    public boolean connect = true;

    /**
     * 套接字客户端
     */
    private Channel channel;

    /**
     * 会话ID
     */
    private UUID uuid;

    /**
     * 会话状态
     */
    private STATUS status;

    /**
     * 会话属性
     */
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    /**
     * 构造函数
     *
     * @param channel 套接字客户端
     */
    Session(Channel channel) {
        this.channel = channel;
        this.uuid = UUID.randomUUID();
        this.status = STATUS.ESTABLISHED;
    }

    /**
     * 标记为删除
     */
    public void delete() {
        this.setStatus(STATUS.DELETABLE);
    }

    /**
     * 清除会话
     */
    void clean() {
        this.channel = null;
        this.uuid = null;
        this.status = null;
        this.attributes = null;
    }

    /**
     * 会话是否断连
     *
     * @return 连接状态
     */
    public boolean isActive() {
        return channel.isActive();
    }

    /**
     * 增加属性
     *
     * @param key   属性键
     * @param value 属性值
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * 获取属性
     *
     * @param key 属性键
     * @return
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 获取状态
     *
     * @return 会话状态
     */
    public STATUS getStatus() {
        return status;
    }

    /**
     * 设定状态
     *
     * @param status 会话状态
     */
    void setStatus(STATUS status) {
        this.status = status;
    }

    /**
     * 获取客户端套接字
     *
     * @return 户端套接字
     */
    Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "Session{" +
                "connect=" + connect +
                ", channel=" + channel.remoteAddress() +
                ", uuid=" + uuid +
                ", status=" + status +
                ", attributes=" + attributes.size() +
                '}';
    }
}
