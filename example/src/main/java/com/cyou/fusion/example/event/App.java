/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.example.event;

import com.cyou.fusion.core.evnet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程间通信
 * <p>
 * Created by zhanglei_js on 2017/5/12.
 */
public class App {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     * 入口
     *
     * @param args 自定义参数
     */
    public static void main(String[] args) throws InterruptedException {

        // 创建一个通信Runnable对象 google
        Runnable google = () -> {
            EventLooper.prepare();
            EventHandler handler = new EventHandler() {
                @Override
                public void handleMessage(Event event) {
                    switch (event.getWhat()) {
                        case 0:
                            LOGGER.info("Google: 处理事件[" + event.getWhat() + "],Event(" + event + ")");
                            // 回调
                            EventBus.INSTANCE.onSuccess("OK", event);
                            break;
                        case 1:
                            LOGGER.info("Google: 处理事件[" + event.getWhat() + "],Event(" + event + ")");
                            // 回调
                            EventBus.INSTANCE.onSuccess("OK", event);
                            break;
                        case 3:
                            LOGGER.info("Google: 未知事件[" + event.getWhat() + "],Event(" + event + ")");
                        default:
                            break;
                    }
                }
            };
            handler.addTags("Google");
            handler.addTags("Android");
            handler.addTags("AlphaGo");
            EventBus.INSTANCE.registerEventHandler(handler);

            while (!Thread.interrupted()) {
                EventLooper.execute();
            }
            System.out.println("结束运行.");

        };

        // 创建一个通信Runnable对象 microsoft
        Runnable microsoft = () -> {
            EventLooper.prepare();
            EventHandler handler = new EventHandler() {
                @Override
                public void handleMessage(Event event) {
                    switch (event.getWhat()) {
                        case 0:
                            LOGGER.info("Microsoft: 处理事件[" + event.getWhat() + "],Event(" + event + ")");
                            // 回调
                            EventBus.INSTANCE.onSuccess("OK", event);
                            break;
                        case 2:
                            LOGGER.info("Microsoft: 处理事件[" + event.getWhat() + "],Event(" + event + ")");
                            // 回调
                            EventBus.INSTANCE.onFailure(new IllegalArgumentException("ERROR"), event);
                            break;
                        case 4:
                            LOGGER.info("microsoft: 未知事件[" + event.getWhat() + "],Event(" + event + ")");
                        default:
                            break;
                    }
                }
            };
            handler.addTags("Windows");
            handler.addTags("VisualStudio");
            handler.addTags("Office");
            EventBus.INSTANCE.registerEventHandler(handler);

            while (!Thread.interrupted()) {
                EventLooper.execute();
            }
            System.out.println("结束运行.");

        };

        // 创建一个通信Runnable对象 apple
        Runnable apple = () -> {
            EventLooper.prepare();
            EventHandler handler = new EventHandler() {
                @Override
                public void handleMessage(Event event) {
                }
            };
            EventBus.INSTANCE.registerEventHandler(handler);

            boolean notice = false;
            while (!Thread.interrupted()) {
                EventLooper.execute();

                // 模拟1秒以后发送消息(仅发送1次)
                try {
                    if (!notice) {
                        TimeUnit.SECONDS.sleep(1);
                        Event<String> e0 = new Event<>(0, "E0", new EventCallback<String>() {
                            @Override
                            public void onSuccess(String message) {
                                LOGGER.info("E0回调 :onSuccess(" + message + ")");
                            }

                            @Override
                            public void onFailure(Throwable ex) {
                                LOGGER.info("E0回调 :onFailure(" + ex.getMessage() + ")");
                            }
                        });
                        Event<String> e1 = new Event<>(1, "E1", new EventCallback<String>() {
                            @Override
                            public void onSuccess(String message) {
                                LOGGER.info("E1回调 :onSuccess(" + message + ")");
                            }

                            @Override
                            public void onFailure(Throwable ex) {
                                LOGGER.info("E1回调 :onFailure(" + ex.getMessage() + ")");
                            }
                        });
                        Event<String> e2 = new Event<>(2, "E2", new EventCallback<String>() {
                            @Override
                            public void onSuccess(String message) {
                                LOGGER.info("E2回调 :onSuccess(" + message + ")");
                            }

                            @Override
                            public void onFailure(Throwable ex) {
                                LOGGER.info("E2回调 :onFailure(" + ex.getMessage() + ")");
                            }
                        });
                        Event e3 = new Event(3, "E3");
                        Event e4 = new Event(4, "E4");
                        EventBus.INSTANCE.broadcast(e0);
                        EventBus.INSTANCE.broadcast(e1);
                        EventBus.INSTANCE.broadcast(e2);
                        EventBus.INSTANCE.broadcast(e3, eventHandler -> {
                            // e3 只发送给Android
                            return eventHandler.getTags().stream().anyMatch(tag -> tag.equals("Android"));
                        });
                        EventBus.INSTANCE.broadcast(e4, eventHandler -> {
                            // e4 只发送给Office
                            return eventHandler.getTags().stream().anyMatch(tag -> tag.equals("Office"));
                        });
                        notice = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("结束运行.");
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(google);
        executorService.submit(microsoft);
        executorService.submit(apple);

        // 运行5秒后结束
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
    }
}
