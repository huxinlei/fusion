/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

import com.cyou.fusion.nio.annotation.Handler;
import com.cyou.fusion.nio.codec.Packet;
import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.cyou.fusion.nio.Session.ATTR_INPUT;
import static com.cyou.fusion.nio.Session.ATTR_OUTPUT;

/**
 * 逻辑处理线程类
 */
public class Tick implements Runnable {

    /**
     * tick 对象唯一ID
     */
    private String id = UUID.randomUUID().toString();

    /**
     * 当前线程处理的会话组
     */
    private Map<Channel, Session> sessions;

    /**
     * 处理逻辑
     */
    private Processor processor;

    /**
     * 消息包处理器
     */
    private PacketHandler[] handlers;

    /**
     * 线程属性
     */
    private Map<String, Object> attributes;

    /**
     * 构造函数
     *
     * @param processor 处理逻辑
     */
    Tick(Processor processor, PacketHandler[] handlers) {
        this.sessions = new ConcurrentHashMap<>();
        this.attributes = new ConcurrentHashMap<>();

        this.processor = processor;

        // this.handlers = handlers;
        // 优化dispatch检索 (最大支持1W)
        this.handlers = new PacketHandler[10000];
        Arrays.stream(handlers).forEach(handler -> this.handlers[handler.getClass().getAnnotation(Handler.class).value()] = handler);
    }

    @Override
    public void run() {

        // 初始化回调
        processor.prepare();

        // Tick处理
        while (!Thread.interrupted()) {
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
                Queue<Packet> inputs = (Queue<Packet>) session.getAttribute(ATTR_INPUT);
                Packet inputPacket;
                int inputCounter = 0;
                // 最多处理20个
                while (inputCounter < 20 && (inputPacket = inputs.poll()) != null) {
                    dispatch(session, inputPacket);
                    inputCounter++;
                }

                // 每个session的tick逻辑
                processor.tick(session);

                // 处理输出
                Queue<Packet> outputs = (Queue<Packet>) session.getAttribute(ATTR_OUTPUT);
                Packet outputPacket;
                int outputCounter = 0;
                // 最多处理10个
                while (outputCounter < 10 && (outputPacket = outputs.poll()) != null) {
                    session.getChannel().write(outputPacket);
                    outputCounter++;
                }
                session.getChannel().flush();

                // 处理断线
                if (!session.isActive() && !session.disconnect) {
                    // 回调断线处理(仅一次，仅被动断线通知，主动关闭不通知)
                    processor.disconnect(session);
                    session.disconnect = true;
                }
            });

            // 自定义tick逻辑处理
            processor.tick();
        }

        // 结束回调
        processor.terminate();

    }

    /**
     * 分配给处理器处理消息
     *
     * @param session 会话
     * @param packet  消息包
     */
    private void dispatch(Session session, Packet packet) {
        // Arrays.stream(this.handlers).filter(handler -> packet.getPacketID() == handler.getClass().getAnnotation(Handler.class).value()).forEach(handler -> handler.handle(session, packet));
        // 优化dispatch检索 (最大支持1W)
        handlers[packet.getPacketID()].handle(session, packet);
    }

    /**
     * 增加一个处理会话
     *
     * @param channel 套接字客户端
     * @param session 会话
     */
    public void putSession(Channel channel, Session session) {
        sessions.put(channel, session);
    }

    /**
     * 获取一个处理会话
     *
     * @param channel 套接字客户端
     * @return session 会话
     */
    public Session getSession(Channel channel) {
        return sessions.get(channel);
    }

    /**
     * 获取所有处理会话
     *
     * @return session 所有会话
     */
    public Collection<Session> getAllSessions() {
        return sessions.values();
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
     * @return 属性键
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 获取Tick ID
     *
     * @return TICK ID
     */
    public String getId() {
        return id;
    }
}
