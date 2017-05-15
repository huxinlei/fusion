package com.cyou.fusion.example.client;

import com.cyou.fusion.core.evnet.Event;
import com.cyou.fusion.core.evnet.EventBus;
import com.cyou.fusion.core.evnet.EventLooper;
import com.cyou.fusion.example.protocol.Account;
import com.cyou.fusion.nio.Processor;
import com.cyou.fusion.nio.Session;
import com.cyou.fusion.nio.codec.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 服务器逻辑处理
 * <p>
 * Created by zhanglei_js on 2017/5/8.
 */
public class ClientProcessor implements Processor {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProcessor.class);

    @Override
    public void prepare() {
        LOGGER.info("处理开始");
    }


    @Override
    public void tick() {
        try {
            // 模拟其他逻辑处理
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick(Session session) {
        LOGGER.info("玩家处理({})", session.toString());
        // 发送登录消息
        if (session.getStatus() == Session.STATUS.ESTABLISHED) {
            Packet packet = new Packet((short) 1, Account.P_ACCOUNT_LOGIN_REQ.newBuilder().setToken(UUID.randomUUID().toString()).build().toByteArray());
            session.sendPacket(packet);
            session.setStatus(Session.STATUS.LOGGING);
        }
    }

    @Override
    public void disconnect(Session session) {
        LOGGER.info("玩家离线({})", session.id());
        // 保存玩家数据
        session.delete();
    }

    @Override
    public void terminate() {
        LOGGER.info("处理结束");
    }
}
