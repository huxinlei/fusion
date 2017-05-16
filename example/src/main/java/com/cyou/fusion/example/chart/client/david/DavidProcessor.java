/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.client.david;

import com.cyou.fusion.example.protocol.Account;
import com.cyou.fusion.example.protocol.Chart;
import com.cyou.fusion.nio.Processor;
import com.cyou.fusion.nio.Session;
import com.cyou.fusion.nio.Tick;
import com.cyou.fusion.nio.codec.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.UUID;

/**
 * 客户端逻辑处理
 * <p>
 * Created by zhanglei_js on 2017/5/8.
 */
class DavidProcessor implements Processor {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DavidProcessor.class);

    @Override
    public void prepare(Tick tick) {
        // 输入处理
        Thread console = new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!tick.getAllSessions().isEmpty()) {
                    Scanner scanner = new Scanner(System.in);
                    Session session = tick.getAllSessions().iterator().next();
                    String message = scanner.nextLine();
                    if (message != null && !message.isEmpty()) {
                        if (message.contains("@")) {
                            String[] info = message.split("@", 2);
                            session.sendPacket(new Packet((short) 5, Chart.P_CHART_SEND_REQ.newBuilder().setMessage(info[1]).setPrivate(true).setName(info[0]).build().toByteArray()));
                        } else {
                            session.sendPacket(new Packet((short) 5, Chart.P_CHART_SEND_REQ.newBuilder().setMessage(message).setPrivate(false).build().toByteArray()));
                        }
                    }
                }
            }
        });
        console.setDaemon(true);
        console.start();
    }


    @Override
    public void tick(Tick tick) {


        // 指定tick间隔
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick(Tick tick, Session session) {
        // 发送登录消息
        if (session.getStatus() == Session.STATUS.ESTABLISHED) {
            Packet packet = new Packet((short) 1, Account.P_ACCOUNT_LOGIN_REQ.newBuilder().setToken(UUID.randomUUID().toString()).setName("David").build().toByteArray());
            session.sendPacket(packet);
            session.setStatus(Session.STATUS.LOGGING);
        }
    }

    @Override
    public void disconnect(Tick tick, Session session) {
        session.delete();
    }

    @Override
    public void terminate(Tick tick) {

    }
}
