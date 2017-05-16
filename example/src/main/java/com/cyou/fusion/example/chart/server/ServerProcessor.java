/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.server;

import com.cyou.fusion.core.evnet.Event;
import com.cyou.fusion.core.evnet.EventBus;
import com.cyou.fusion.core.evnet.EventHandler;
import com.cyou.fusion.core.evnet.EventLooper;
import com.cyou.fusion.example.protocol.Account;
import com.cyou.fusion.example.protocol.Chart;
import com.cyou.fusion.nio.Processor;
import com.cyou.fusion.nio.Session;
import com.cyou.fusion.nio.Tick;
import com.cyou.fusion.nio.codec.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器逻辑处理
 * <p>
 * Created by zhanglei_js on 2017/5/8.
 */
public class ServerProcessor implements Processor {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerProcessor.class);

    @Override
    public void prepare(Tick tick) {
        // 初始化线程间事件
        EventLooper.prepare();
        EventHandler handler = new EventHandler() {
            @Override
            public void handleMessage(Event event) {
                switch (event.getWhat()) {
                    case 100:
                        // 上线通知
                        tick.getAllSessions().stream().filter(session ->
                                !session.getAttribute("name").equals(event.getData())
                        ).forEach(session ->
                                session.sendPacket(new Packet((short) 3, Account.P_ACCOUNT_LOGIN_NOTICE.newBuilder().setName(event.getData().toString()).build().toByteArray())));
                        break;
                    case 200:
                        // 下线通知
                        tick.getAllSessions().stream().filter(other ->
                                !other.getAttribute("name").equals(event.getData())
                        ).forEach(other ->
                                other.sendPacket(new Packet((short) 4, Account.P_ACCOUNT_LOGOUT_NOTICE.newBuilder().setName(event.getData().toString()).build().toByteArray())));
                        break;
                    case 300:
                        // 聊天通知
                        String[] data = event.getData().toString().split("@", 2);
                        tick.getAllSessions().stream().forEach(client -> {
                                    client.sendPacket(new Packet((short) 6, Chart.P_CHART_RECEIVE_NOTICE.newBuilder().setName(data[0]).setMessage(data[1]).build().toByteArray()));
                                }
                        );
                        break;
                    case 400:
                        // 聊天通知
                        String[] info = event.getData().toString().split("@", 3);
                        tick.getAllSessions().stream().filter(session -> session.getAttribute("name").equals(info[1])).forEach(client -> {
                                    client.sendPacket(new Packet((short) 6, Chart.P_CHART_RECEIVE_NOTICE.newBuilder().setName(info[0]).setMessage(info[2]).build().toByteArray()));
                                }
                        );
                        break;
                    default:
                        break;
                }
            }
        };
        EventBus.INSTANCE.registerEventHandler(handler);
    }

    @Override
    public void tick(Tick tick) {
        // 执行线程间事件
        EventLooper.execute();

        // 指定tick间隔
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick(Tick tick, Session session) {

    }

    @Override
    public void disconnect(Tick tick, Session session) {
        // 通知其他人客户端断开
        EventBus.INSTANCE.broadcast(new Event(200, session.getAttribute("name")));

        // 移除TAG
        EventHandler eventHandler = EventBus.INSTANCE.getHandler(Thread.currentThread().getId());
        eventHandler.removeTags("name:" + session.getAttribute("name"));

        // 减少负载
        AtomicInteger load = (AtomicInteger) tick.getAttribute("load");
        load.decrementAndGet();
        tick.setAttribute("load", load);

        session.delete();
    }

    @Override
    public void terminate(Tick tick) {
        LOGGER.info("处理结束");
    }
}
