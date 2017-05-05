/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

import com.cyou.fusion.nio.codec.AbstractPacket;
import io.netty.channel.Channel;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static com.cyou.fusion.nio.Session.ATTR_INPUT;
import static com.cyou.fusion.nio.Session.ATTR_OUTPUT;

/**
 * 逻辑处理线程类
 */
public class Tick implements Runnable {

    /**
     * 当前线程处理的会话组
     */
    private Map<Channel, Session> sessions;

    /**
     * 处理逻辑
     */
    private Processor processor;

    /**
     * 构造函数
     *
     * @param processor 处理逻辑
     */
    public Tick(Processor processor) {
        this.sessions = new ConcurrentHashMap<>();
        this.processor = processor;
    }

    @Override
    public void run() {
        // 清除无效的套接字对象
        Iterator<Map.Entry<Channel, Session>> iterator = sessions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Channel, Session> entry = iterator.next();
            Channel channel = entry.getKey();
            Session session = entry.getValue();
            if (session.getStatus() == Session.STATUS.DELETABLE) {
                session.clean();
                channel.close();
                iterator.remove();
            }
        }

        // 逻辑处理每个客户端会话
        sessions.values().stream().forEach(session -> {

            // 处理输入
            Object inputs = session.getAttribute(ATTR_INPUT);
            if (inputs != null) {
                // TODO 限定处理个数
                ((Queue<AbstractPacket>) inputs).stream().forEach(packet -> packet.handle(session));
            }

            // 处理断线
            if (!session.isActive()) {
                // TODO 回调客户端处理断线
            }

            // 处理输出
            Object outputs = session.getAttribute(ATTR_OUTPUT);
            if (outputs != null && session.isActive()) {
                // TODO 限定处理个数
                ((Queue<AbstractPacket>) outputs).stream().forEach(packet -> session.getChannel().write(packet));
                session.getChannel().flush();
            }
        });

        // 自定义tick逻辑处理
        processor.process();
    }

    /**
     * 增加一个处理会话
     *
     * @param channel 套接字客户端
     * @param session 会话
     */
    public void put(Channel channel, Session session) {
        sessions.put(channel, session);
    }

    /**
     * 获取一个处理会话
     *
     * @param channel 套接字客户端
     * @return session 会话
     */
    public Session get(Channel channel) {
        return sessions.get(channel);
    }
}
