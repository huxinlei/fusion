/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.packet.account;

import com.cyou.fusion.nio.annotation.Packet;
import com.cyou.fusion.nio.codec.AbstractPacket;
import com.cyou.fusion.nio.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录包
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
@Packet(1)
public class LoginPacket extends AbstractPacket {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPacket.class);

    @Override
    public void handle(Session session) {
        LOGGER.debug("登录处理:{}|{}", session.toString(), this.toString());
    }
}
