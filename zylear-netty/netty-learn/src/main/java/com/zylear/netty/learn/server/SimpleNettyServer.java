package com.zylear.netty.learn.server;

import com.zylear.netty.learn.netty.SimpleChatServerHandler;
import com.zylear.netty.learn.netty.SimpleChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

/**
 * @author 28444
 * @date 2018/1/9.
 */

public class SimpleNettyServer {

    private static Integer port = 9090;


    public void startServer() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //行分割
                        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        pipeline.addLast("handler", new SimpleBlokusServerHandler());
                    }
                });


        // 绑定端口，开始接收进来的连接
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        System.out.println("server 启动了");

//        while (true) {
//            Thread.sleep(5 * 1000);
//            System.out.println(SimpleChatServerHandler.channels.size());
//            System.out.println("ggg  "+SimpleChatServerHandler.channelGroup.size());
//        }


        channelFuture.channel().closeFuture().sync();
        /**CloseFuture异步方式关闭*/
        System.out.println("dfdsfsdf1111");
//        Thread.sleep(24 * 60 * 60 * 1000);

        // 等待服务器  socket 关闭 。
        // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
//        serverBootstrap.channel().closeFuture().sync();

    }

    public static void main(String[] args) throws InterruptedException {
        new SimpleNettyServer().startServer();
    }

}
