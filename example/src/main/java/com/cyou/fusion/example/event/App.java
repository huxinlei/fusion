package com.cyou.fusion.example.event;

import com.cyou.fusion.core.evnet.Event;
import com.cyou.fusion.core.evnet.EventBus;
import com.cyou.fusion.core.evnet.EventHandler;
import com.cyou.fusion.core.evnet.EventLooper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 进程间通信
 * <p>
 * Created by zhanglei_js on 2017/5/12.
 */
public class App {

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
                        case 1:
                            System.out.println("已知事件:Thread(" + Thread.currentThread().getName() + "),Event(" + event + ")");
                            // 回调
                            EventBus.INSTANCE.callback(() -> System.out.println("回调:Thread(" + Thread.currentThread().getName() + "),Event(" + event + ")"), event);
                            break;
                        default:
                            System.out.println("未知事件:Thread(" + Thread.currentThread().getName() + "),Event(" + event + ")");
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
                        case 2:
                            System.out.println("已知事件:Thread(" + Thread.currentThread().getName() + "),Event(" + event + ")");
                            // 回调
                            EventBus.INSTANCE.callback(() -> System.out.println("回调:Thread(" + Thread.currentThread().getName() + "),Event(" + event + ")"), event);
                            break;
                        default:
                            System.out.println("未知事件:Thread(" + Thread.currentThread().getName() + "),Event(" + event + ")");
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

                // 模拟1秒以后发送消息
                try {
                    if (!notice) {
                        TimeUnit.SECONDS.sleep(1);
                        Event e1 = new Event(1, "E1");
                        Event e2 = new Event(2, "E2");
                        Event e3 = new Event(3, "E3");
                        Event e4 = new Event(4, "E4");
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

        ExecutorService executorService = Executors.newFixedThreadPool(3, r -> new Thread(r, r.hashCode() + "@Corporation"));
        executorService.submit(google);
        executorService.submit(microsoft);
        executorService.submit(apple);

        // 运行5秒后结束
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
    }
}
