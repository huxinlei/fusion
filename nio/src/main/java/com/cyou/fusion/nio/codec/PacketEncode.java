/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 消息包编码器
 * <p>
 * Created by john on 2017/3/24.
 */
public class PacketEncode extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet msg, ByteBuf byteBuf) throws Exception {

        short packetID = msg.getPacketID();
        byte body[] = msg.getBody();
        int packetLen = body.length;

        byteBuf.writeShort(packetID);
        byteBuf.writeInt(packetLen);
        byteBuf.writeBytes(body);
    }
}
