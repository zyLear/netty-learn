package com.zylear.netty.learn.server;

import com.zylear.netty.learn.netty.SimpleChatServerHandler;
import com.zylear.netty.learn.netty.SimpleChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * @author 28444
 * @date 2018/1/9.
 */
@Component
public class SimpleNettyServer {

    private static Integer port = 9090;




    public void startServer() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChatServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);


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
