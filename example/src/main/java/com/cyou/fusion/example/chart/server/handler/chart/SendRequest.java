/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.server.handler.chart;

import com.cyou.fusion.core.evnet.Event;
import com.cyou.fusion.core.evnet.EventBus;
import com.cyou.fusion.example.protocol.Chart;
import com.cyou.fusion.nio.PacketHandler;
import com.cyou.fusion.nio.Session;
import com.cyou.fusion.nio.annotation.Handler;
import com.cyou.fusion.nio.codec.Packet;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天包
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
@Handler(5)
public class SendRequest implements PacketHandler {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SendRequest.class);

    @Override
    public void handle(Session session, Packet packet) {
        try {
            Chart.P_CHART_SEND_REQ request = Chart.P_CHART_SEND_REQ.parseFrom(packet.getBody());

            if (request.getPrivate()) {
                String name = request.getName();
                String message = request.getMessage();
                // 通知对方和自己
                EventBus.INSTANCE.broadcast(new Event(400, session.getAttribute("name") + "@" + name + "@" + message), eventHandler -> eventHandler.getTags().stream().anyMatch(tag -> tag.equals("name:" + request.getName())));
                session.sendPacket(new Packet((short) 6, Chart.P_CHART_RECEIVE_NOTICE.newBuilder().setName(session.getAttribute("name").toString()).setMessage(message).build().toByteArray()));

            } else {
                // 通知所有人
                EventBus.INSTANCE.broadcast(new Event(300, session.getAttribute("name") + "@" + request.getMessage()));
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("非法消息包", e);
        }
    }
}
