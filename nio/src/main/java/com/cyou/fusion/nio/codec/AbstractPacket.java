/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio.codec;

import com.cyou.fusion.nio.Session;

import java.util.Arrays;

/**
 * 抽象消息包
 * <p>
 * Created by zhanglei_js on 2017/2/6.
 */
public abstract class AbstractPacket {

    /**
     * 消息包ID
     */
    private short packetID;

    /**
     * 消息包长度
     */
    private int packetLength;

    /**
     * 消息包内容
     */
    private byte[] body = null;


    /**
     * Gets packet id.
     *
     * @return the packet id
     */
    public short getPacketID() {
        return packetID;
    }

    /**
     * Sets packet id.
     *
     * @param packetID the packet id
     */
    public void setPacketID(short packetID) {
        this.packetID = packetID;
    }

    /**
     * Gets packet length.
     *
     * @return the packet length
     */
    public int getPacketLength() {
        return packetLength;
    }

    /**
     * Sets packet length.
     *
     * @param packetLength the packet length
     */
    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
    }

    /**
     * Get body byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "AbstractPacket{" +
                "packetID=" + packetID +
                ", packetLength=" + packetLength +
                ", body=" + Arrays.toString(body) +
                '}';
    }

    /**
     * 消息处理
     *
     * @param session 会话
     */
    public abstract void handle(Session session);
}
