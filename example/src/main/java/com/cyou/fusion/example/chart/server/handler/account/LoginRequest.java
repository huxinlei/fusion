/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.server.handler.account;

import com.cyou.fusion.core.evnet.Event;
import com.cyou.fusion.core.evnet.EventBus;
import com.cyou.fusion.core.evnet.EventHandler;
import com.cyou.fusion.example.protocol.Account;
import com.cyou.fusion.nio.PacketHandler;
import com.cyou.fusion.nio.Session;
import com.cyou.fusion.nio.annotation.Handler;
import com.cyou.fusion.nio.codec.Packet;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录包
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
@Handler(1)
public class LoginRequest implements PacketHandler {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRequest.class);

    @Override
    public void handle(Session session, Packet packet) {
        try {
            Account.P_ACCOUNT_LOGIN_REQ request = Account.P_ACCOUNT_LOGIN_REQ.parseFrom(packet.getBody());
            session.setAttribute("name", request.getName());

            session.setStatus(Session.STATUS.LOGGED);

            // 设置TAG
            EventHandler eventHandler = EventBus.INSTANCE.getHandler(Thread.currentThread().getId());
            eventHandler.addTags("name:" + request.getName());
            
            // 通知其他所有人
            EventBus.INSTANCE.broadcast(new Event(100, request.getName()));

            // 返回结果
            Packet response = new Packet((short) 2, Account.P_ACCOUNT_LOGIN_RES.newBuilder()
                    .setResult(true)
                    .setName(request.getName())
                    .setMessage("Hi, " + request.getName() + ". Welcome to the chart room!")
                    .build().toByteArray());
            session.sendPacket(response);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("非法消息包", e);
        }
    }
}
