package com.cyou.fusion.example.server;

import com.cyou.fusion.example.server.handler.account.LoginRequest;
import com.cyou.fusion.nio.PacketHandler;
import com.cyou.fusion.nio.Server;

import java.net.InetSocketAddress;

/**
 * 服务器应用入口
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
public class ServerApp {

    public static void main(String[] args) {
        Server.Builder builder = new Server.Builder(ServerProcessor::new).selector((ticks, channel) -> ticks[((InetSocketAddress) channel.remoteAddress()).getPort() % ticks.length]);
        Server server = builder.host("localhost").port(8080).pool(2).handler(new PacketHandler[]{new LoginRequest()}).build();
        server.start();
    }
}
