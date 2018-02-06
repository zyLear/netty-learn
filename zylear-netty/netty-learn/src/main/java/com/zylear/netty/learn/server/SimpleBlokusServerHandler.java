package com.zylear.netty.learn.server;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.TransferBean;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import com.zylear.netty.learn.manager.MessageManager;
import com.zylear.netty.learn.queue.MessageQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author 28444
 * @date 2018/1/9.
 */

public class SimpleBlokusServerHandler extends SimpleChannelInboundHandler<MessageBean> {


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        Channel incoming = ctx.channel();
//        MessageManager.connectChannelGroup.add(incoming);
//        System.out.println("handlerAdded");

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        Channel incoming = ctx.channel();
//        MessageManager.connectChannelGroup.remove(incoming);
//        System.out.println("removed");
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
        Channel channel = ctx.channel();
        MessageQueue.getInstance().put(new TransferBean(MessageBean.QUIT, ctx.channel()));
        System.out.println("client " + channel.remoteAddress() + " 异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageBean message) throws Exception {
        MessageQueue.getInstance().put(new TransferBean(message, ctx.channel()));
    }

}