/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.chart.client.jim;

import com.cyou.fusion.example.chart.client.handler.account.LoginNotice;
import com.cyou.fusion.example.chart.client.handler.account.LoginResponse;
import com.cyou.fusion.example.chart.client.handler.account.LogoutNotice;
import com.cyou.fusion.example.chart.client.handler.chart.ReceiveNotice;
import com.cyou.fusion.nio.Client;
import com.cyou.fusion.nio.PacketHandler;

/**
 * 服务器应用入口
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
public class JimApp {

    public static void main(String[] args) throws InterruptedException {
        // 启动客户端
        Client jim = new Client.Builder(new JimProcessor()).host("localhost").port(8080)
                .handler(new PacketHandler[]{new LoginResponse(), new LoginNotice(), new LogoutNotice(), new ReceiveNotice()}).build();
        jim.start();
    }
}
