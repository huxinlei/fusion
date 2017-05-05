/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 消息包解码器
 * <p>
 * Created by john on 2017/3/24.
 */
public class PacketDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        // 每次最多收十个包
        for (int i = 0; i < 10; i++) {

            // 不够一个包头的长度
            int bufLen = byteBuf.readableBytes();
            if (bufLen < 6) {
                return;
            }

            byteBuf.markReaderIndex();

            short packetID = byteBuf.readShort();
            int packetLength = byteBuf.readInt();

            if (packetID < 0 || packetLength <= 0) {
                throw new IllegalStateException("AbstractPacket format invalid.");
            }

            // 不够一个包体的长度,重置读索引,继续接收
            if (byteBuf.readableBytes() < packetLength) {
                byteBuf.resetReaderIndex();
                return;
            }

            // 根据消息包ID生成消息包

            AbstractPacket packet = PacketFactory.INSTANCE.newPacket(packetID);
            if (packet == null) {
                throw new IllegalStateException("Packet create error.");
            }
            packet.setPacketID(packetID);
            packet.setPacketLength(packetLength);

            byte body[] = new byte[packetLength];
            byteBuf.readBytes(body);
            packet.setBody(body);

            list.add(packet);
        }

    }

}
