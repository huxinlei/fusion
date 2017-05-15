package com.cyou.fusion.example.server;

import com.cyou.fusion.nio.Processor;
import com.cyou.fusion.nio.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器逻辑处理
 * <p>
 * Created by zhanglei_js on 2017/5/8.
 */
public class ServerProcessor implements Processor {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerProcessor.class);

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
