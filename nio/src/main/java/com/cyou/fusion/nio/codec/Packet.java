/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio.codec;

import java.util.Arrays;

/**
 * 抽象消息包
 * <p>
 * Created by zhanglei_js on 2017/2/6.
 */
public class Packet {

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
     * 构造函数
     *
     * @param packetID 消息ID
     * @param body     消息体
     */
    public Packet(short packetID, byte[] body) {
        this.packetID = packetID;
        this.body = body;
        this.packetLength = body.length;
    }

    /**
     * Gets packet id.
     *
     * @return the packet id
     */
    public short getPacketID() {
        return packetID;
    }

    /**
     * Get body byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBody() {
        return body;
    }


    @Override
    public String toString() {
        return "Packet{" +
                "packetID=" + packetID +
                ", packetLength=" + packetLength +
                ", body=" + Arrays.toString(body) +
                '}';
    }


}
