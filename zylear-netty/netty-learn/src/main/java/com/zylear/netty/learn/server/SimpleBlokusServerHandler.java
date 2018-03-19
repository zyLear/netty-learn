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
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author 28444
 * @date 2018/1/9.
 */

public class SimpleBlokusServerHandler extends SimpleChannelInboundHandler<MessageBean> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBlokusServerHandler.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        Channel incoming = ctx.channel();
//        MessageManager.connectChannelGroup.add(incoming);
//        System.out.println("handlerAdded");

    }

//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
////        Channel incoming = ctx.channel();
////        MessageManager.connectChannelGroup.remove(incoming);
////        System.out.println("removed");
//    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelActive");
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("channelInactive");
//    }


//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
////        Channel channel = ctx.channel();
////        MessageQueue.getInstance().put(new TransferBean(MessageBean.QUIT, ctx.channel()));
////        logger.info("client:{} handlerRemoved. ", channel.remoteAddress());
////        ctx.close();
//    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Channel channel = ctx.channel();
        //  MessageQueue.getInstance().put(new TransferBean(MessageBean.QUIT, ctx.channel()));
        //   ctx.close();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                logger.info("client:{} channelInactive. ", channel.remoteAddress());
                ctx.close();
            }/* else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new PingMessage());
            }*/
        }
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        MessageQueue.getInstance().put(new TransferBean(MessageBean.QUIT, ctx.channel()));
        logger.info("client:{} channelInactive. ", channel.remoteAddress());
        ctx.close();
        //  channelInactive  ->  handlerRemoved
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        Channel channel = ctx.channel();
        MessageQueue.getInstance().put(new TransferBean(MessageBean.QUIT, ctx.channel()));
        logger.info("client:{} exceptionCaught. ", channel.remoteAddress(), e);
        // 当出现异常就关闭连接
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageBean message) throws Exception {
        MessageQueue.getInstance().put(new TransferBean(message, ctx.channel()));
    }

}