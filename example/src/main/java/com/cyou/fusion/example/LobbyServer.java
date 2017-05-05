package com.cyou.fusion.example;

import com.cyou.fusion.example.packet.account.LoginPacket;
import com.cyou.fusion.nio.Server;
import com.cyou.fusion.nio.codec.PacketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 大厅服务器类
 * <p>
 * Created by zhanglei_js on 2017/5/5.
 */
public class LobbyServer {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyServer.class);

    public static void main(String[] args) {

        // 注册处理包
        PacketFactory.INSTANCE.register(LoginPacket.class);


        Server.Builder builder = new Server.Builder(() -> () -> {
            // 每次tick的回调
            LOGGER.info(Thread.currentThread().getName());

            try {
                // 模拟其他逻辑处理
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Server server = builder.host("localhost").port(50007).pool(2).build();
        server.start();

        while (true) {

        }
    }
}
