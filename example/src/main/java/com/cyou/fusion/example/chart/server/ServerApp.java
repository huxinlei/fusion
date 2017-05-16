/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.server;

import com.cyou.fusion.example.chart.server.handler.account.LoginRequest;
import com.cyou.fusion.example.chart.server.handler.chart.SendRequest;
import com.cyou.fusion.nio.PacketHandler;
import com.cyou.fusion.nio.Server;
import com.cyou.fusion.nio.Tick;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器聊天应用入口
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
public class ServerApp {

    public static void main(String[] args) {
        Server.Builder builder = new Server.Builder(ServerProcessor::new).selector((ticks, channel) -> {
            // 负债均衡
            Tick tick = Arrays.stream(ticks).sorted((t1, t2) -> {
                if (t1.getAttribute("load") == null) {
                    t1.setAttribute("load", new AtomicInteger(0));
                }
                if (t2.getAttribute("load") == null) {
                    t2.setAttribute("load", new AtomicInteger(0));
                }
                AtomicInteger l1 = (AtomicInteger) t1.getAttribute("load");
                AtomicInteger l2 = (AtomicInteger) t2.getAttribute("load");
                return l1.get() == l2.get() ? 0 : l1.get() > l2.get() ? 1 : -1;
            }).findFirst().orElse(null);
            if (tick == null) {
                throw new IllegalStateException("无可用线程");
            }
            AtomicInteger load = (AtomicInteger) tick.getAttribute("load");
            load.incrementAndGet();
            tick.setAttribute("load", load);
            return tick;
        });
        Server server = builder.host("localhost").port(8080).pool(3).handler(new PacketHandler[]{new LoginRequest(), new SendRequest()}).build();
        server.start();
    }
}
