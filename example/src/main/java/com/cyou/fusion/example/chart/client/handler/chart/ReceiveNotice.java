/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.client.handler.chart;

import com.cyou.fusion.example.protocol.Chart;
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
@Handler(6)
public class ReceiveNotice implements PacketHandler {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveNotice.class);

    @Override
    public void handle(Session session, Packet packet) {
        try {
            Chart.P_CHART_RECEIVE_NOTICE response = Chart.P_CHART_RECEIVE_NOTICE.parseFrom(packet.getBody());
            LOGGER.info("{}:{}", response.getName(), response.getMessage());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
