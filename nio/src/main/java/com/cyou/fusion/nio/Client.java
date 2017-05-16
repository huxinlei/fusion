/*
 * Copyright 2017 The Changyou Fusion Framework
 */
package com.cyou.fusion.nio;

import com.cyou.fusion.nio.codec.Packet;
import com.cyou.fusion.nio.codec.PacketDecode;
import com.cyou.fusion.nio.codec.PacketEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.cyou.fusion.nio.Session.ATTR_INPUT;

/**
 * Netty的NIO套接字客户端类
 * <p>
 * Created by zhanglei_js on 2017/2/6.
 */
public final class Client {

    /**
     * 日志打印
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    /**
     * Netty处理线程组
     */
    private EventLoopGroup workerGroup;

    /**
     * 连接IP
     */
    private String host;

    /**
     * 连接端口
     */
    private int port;

    /**
     * 处理线程对象
     */
    private Tick tick;

    /**
     * 处理线程调度池
     */
    private ExecutorService tickService;

    /**
     * 构造函数
     *
     * @param builder 构建器
     */
    private Client(Builder builder) {

        this.workerGroup = new NioEventLoopGroup(1);

        this.port = builder.port;
        this.host = builder.host;

        // 初始化处理线程
        tickService = Executors.newFixedThreadPool(1);
        tick = new Tick(builder.processor, builder.handlers);

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
            Session session;

            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

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
                                    tick.putSession(ctx.channel(), session);
                                }


                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    Object inputs = session.getAttribute(ATTR_INPUT);
                                    ((Queue<Packet>) inputs).add((Packet) msg);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    LOGGER.error("服务器连接异常", cause);
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                                    LOGGER.info("服务器连接断开 {}", address);

                                    // 清理session引用，防止ChannelInboundHandler GC不掉，而导致内存溢出
                                    session = null;
                                }
                            });
                        }
                    });

            b.connect(host, port).sync().addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    LOGGER.info("服务器连接正常");
                    tickService.submit(tick);
                } else {
                    LOGGER.error("服务器连接异常", future.cause());
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
         * 处理逻辑
         */
        private Processor processor;

        /**
         * 消息包处理器
         */
        private PacketHandler[] handlers;

        /**
         * 构造方法
         *
         * @param processor 处理逻辑
         */
        public Builder(Processor processor) {
            this.processor = processor;
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
        public Client build() {
            return new Client(this);
        }
    }

}
