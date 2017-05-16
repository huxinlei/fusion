/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

import com.cyou.fusion.nio.codec.Packet;
import com.cyou.fusion.nio.codec.PacketDecode;
import com.cyou.fusion.nio.codec.PacketEncode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.*;

import static com.cyou.fusion.nio.Session.ATTR_INPUT;

/**
 * Netty的NIO套接字服务器类
 * <p>
 * Created by zhanglei_js on 2017/2/6.
 */
public final class Server {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * Netty处理线程组
     */
    private EventLoopGroup bossGroup;

    /**
     * Netty处理线程组
     */
    private EventLoopGroup workerGroup;

    /**
     * 线程选择器
     */
    private Selector selector;

    /**
     * 监听IP
     */
    private String host;

    /**
     * 监听端口
     */
    private int port;

    /**
     * 处理线程数
     */
    private int pool;

    /**
     * 处理线程对象
     */
    private Tick[] ticks;

    /**
     * 处理线程调度池
     */
    private ExecutorService tickService;

    /**
     * 构造函数
     *
     * @param builder 构建器
     */
    private Server(Builder builder) {

        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();

        this.port = builder.port;
        this.host = builder.host;
        this.pool = builder.pool;

        // 初始化处理线程
        tickService = Executors.newFixedThreadPool(this.pool);
        ticks = new Tick[this.pool];
        for (int i = 0; i < this.pool; i++) {
            ticks[i] = new Tick(builder.factory.newProcessor(), builder.handlers);
        }

        // 线程选择器
        if (builder.selector == null) {
            this.selector = (ticks, channel) -> {
                // 默认根据端口选择
                InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
                return ticks[address.getPort() % pool];
            };
        } else {
            this.selector = builder.selector;
        }

    }

    /**
     * 启动套接字服务器
     */
    public void start() {
        bootstrap();
    }

    /**
     * 启动引导处理
     */
    private void bootstrap() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new PacketDecode());
                            p.addLast(new PacketEncode());
                            p.addLast(new ChannelInboundHandlerAdapter() {

                                private Session session;

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    session = new Session(ctx.channel());
                                    // 将session交给某个线程访问处理
                                    Tick tick = selector.select(ticks, ctx.channel());
                                    tick.putSession(ctx.channel(), session);
                                }


                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    Object inputs = session.getAttribute(ATTR_INPUT);
                                    ((Queue<Packet>) inputs).add((Packet) msg);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    LOGGER.error("客户端连接异常", cause);
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                                    LOGGER.info("客户端连接断开 {}", address);

                                    // 清理session引用，防止ChannelInboundHandler GC不掉，而导致内存溢出
                                    session = null;
                                }
                            });
                        }
                    });

            b.bind(host, port).sync().addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    LOGGER.info("服务器启动正常");
                    Arrays.stream(ticks).forEach(tick -> tickService.submit(tick));
                } else {
                    LOGGER.error("服务器启动异常", future.cause());
                }
            });
        } catch (Throwable throwable) {
            LOGGER.error("服务器启动异常", throwable);
        }
    }

    /**
     * 关闭套接字服务器并停止逻辑处理线程
     */
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        tickService.shutdown();
    }

    /**
     * 套接字服务器对象构建器
     */
    public static class Builder {

        /**
         * 监听IP
         */
        private String host = "0.0.0.0";

        /**
         * 监听端口
         */
        private int port = 8080;

        /**
         * 处理逻辑线程数
         */
        private int pool = 1;

        /**
         * 消息包处理器
         */
        private PacketHandler[] handlers;

        /**
         * 处理逻辑工厂
         */
        private ProcessorFactory factory;

        /**
         * 线程选择器
         */
        private Selector selector;

        /**
         * 构造方法
         *
         * @param factory 处理逻辑工厂
         */
        public Builder(ProcessorFactory factory) {
            this.factory = factory;
        }

        /**
         * 指定监听端口
         *
         * @param port 监听端口
         * @return 套接字服务器对象构建器
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * 指定监听地址
         *
         * @param host 监听地址
         * @return 套接字服务器对象构建器
         */
        public Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * 指定逻辑处理线程数
         *
         * @param pool 线程数
         * @return 套接字服务器对象构建器
         */
        public Builder pool(int pool) {
            this.pool = pool;
            return this;
        }

        /**
         * 线程选择器
         *
         * @param selector 线程选择器
         * @return 套接字服务器对象构建器
         */
        public Builder selector(Selector selector) {
            this.selector = selector;
            return this;
        }

        /**
         * 注册消息包处理器
         *
         * @param handlers 消息包处理器
         * @return 套接字服务器对象构建器
         */
        public Builder handler(PacketHandler[] handlers) {
            this.handlers = new PacketHandler[handlers.length];
            System.arraycopy(handlers, 0, this.handlers, 0, handlers.length);
            return this;
        }

        /**
         * 构建套接字服务器对象
         *
         * @return 套接字服务器对象
         */
        public Server build() {
            return new Server(this);
        }
    }

}
