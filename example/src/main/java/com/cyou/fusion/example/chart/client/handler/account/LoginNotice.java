/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.client.handler.account;

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
@Handler(3)
public class LoginNotice implements PacketHandler {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginNotice.class);

    @Override
    public void handle(Session session, Packet packet) {
        try {
            Account.P_ACCOUNT_LOGIN_NOTICE response = Account.P_ACCOUNT_LOGIN_NOTICE.parseFrom(packet.getBody());
            LOGGER.info("SYSTEM: {} enter the room.", response.getName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
