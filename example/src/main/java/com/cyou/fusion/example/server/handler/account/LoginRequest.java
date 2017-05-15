/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.server.handler.account;

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
            LOGGER.info("消息内容：{}", request.getToken());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        LOGGER.info("登录处理({}):{}|{}", this.getClass().getSimpleName(), session.toString(), packet.toString());
        session.setStatus(Session.STATUS.LOGGED);

        // 返回结果
        Packet response = new Packet((short) 2, Account.P_ACCOUNT_LOGIN_RES.newBuilder().setResult(true).build().toByteArray());
        session.sendPacket(response);
    }
}
