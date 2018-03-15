package com.zylear.netty.learn.config;

import com.zylear.netty.learn.server.MessageDecoder;
import com.zylear.netty.learn.server.MessageEncoder;
import com.zylear.netty.learn.server.SimpleBlokusServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Configuration
public class ServerConfig {

    @Bean(value = "blokusServer")
    public ServerBootstrap blokusServer() {
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
                        pipeline.addLast("decoder", new MessageDecoder());
                        pipeline.addLast("encoder", new MessageEncoder());
                        pipeline.addLast(new IdleStateHandler(10, 60 * 60 * 24, 0, TimeUnit.SECONDS));
                        //  pipeline.addLast(new HeartbeatHandler());
                        pipeline.addLast("handler", new SimpleBlokusServerHandler());
                    }
                });
        return serverBootstrap;
    }

}
