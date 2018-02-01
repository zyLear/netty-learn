package com.zylear.netty.learn.config;

import com.zylear.netty.learn.manager.BlokusMessageManager;
import com.zylear.netty.learn.netty.SimpleChatServerInitializer;
import com.zylear.netty.learn.server.SimpleBlokusServerHandler;
import com.zylear.netty.learn.server.SimpleBlokusServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Configuration
public class ServerConfig {

    @Bean
    public SimpleBlokusServerInitializer simpleBlokusServerInitializer(BlokusMessageManager messageManager) {
        return new SimpleBlokusServerInitializer(messageManager);
    }

    @Bean(value = "blokusServer")
    public ServerBootstrap blokusServer(SimpleBlokusServerInitializer simpleBlokusServerInitializer) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(simpleBlokusServerInitializer)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return serverBootstrap;
    }

}
