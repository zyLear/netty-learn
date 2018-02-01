package com.zylear.netty.learn.server;


import com.zylear.netty.learn.manager.BlokusMessageManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author 28444
 * @date 2018/1/9.
 */
public class SimpleBlokusServerInitializer extends ChannelInitializer<SocketChannel> {

    private BlokusMessageManager blokusMessageManager;

    public SimpleBlokusServerInitializer(BlokusMessageManager blokusMessageManager) {
        this.blokusMessageManager = blokusMessageManager;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //行分割
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", new SimpleBlokusServerHandler(blokusMessageManager));

        System.out.println("client " + ch.remoteAddress() + " 连接上");
    }
}