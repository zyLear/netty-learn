package com.zylear.netty.learn.server;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.manager.BlokusMessageManager;
import com.zylear.netty.learn.queue.MessageQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 28444
 * @date 2018/1/9.
 */

public class SimpleBlokusServerHandler extends SimpleChannelInboundHandler<MessageBean> {


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        BlokusMessageManager.connectChannelGroup.add(incoming);
        System.out.println("handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        BlokusMessageManager.connectChannelGroup.remove(incoming);
        System.out.println("removed");
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelActive");
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelInactive");
//    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println("client " + incoming.remoteAddress() + " 异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageBean messageBean) throws Exception {
        MessageQueue.getInstance().put(messageBean);
    }
}