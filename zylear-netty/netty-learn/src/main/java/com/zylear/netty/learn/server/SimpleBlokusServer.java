package com.zylear.netty.learn.server;

import com.zylear.netty.learn.netty.SimpleChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 28444
 * @date 2018/1/9.
 */
@Component
public class SimpleBlokusServer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBlokusServer.class);

    private ServerBootstrap blokusServer;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static Integer port = 9090;

    @PostConstruct
    public void startServer() {

        // 绑定端口，开始接收进来的连接
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    ChannelFuture channelFuture = blokusServer.bind(port).sync();
                    System.out.println("server 启动了");
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Autowired
    public void setBlokusServer(@Qualifier("blokusServer") ServerBootstrap blokusServer) {
        this.blokusServer = blokusServer;
    }

    public static void main(String[] args) throws InterruptedException {
        new SimpleBlokusServer().startServer();
    }

}
