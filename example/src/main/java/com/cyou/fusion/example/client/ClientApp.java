package com.cyou.fusion.example.client;

import com.cyou.fusion.example.client.handler.account.LoginResponse;
import com.cyou.fusion.nio.Client;
import com.cyou.fusion.nio.PacketHandler;

/**
 * 服务器应用入口
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
public class ClientApp {

    public static void main(String[] args) throws InterruptedException {
        Client.Builder builder = new Client.Builder(new ClientProcessor());
        Client client = builder.host("localhost").port(8080).handler(new PacketHandler[]{new LoginResponse()}).build();
        client.start();
    }
}
