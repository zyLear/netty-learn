package com.zylear.netty.learn.client;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.enums.BlokusColor;
import com.zylear.proto.BlokusOuterClass.BLOKUSRoomPlayerInfo;
import com.zylear.proto.BlokusOuterClass.BLOKUSRoomPlayerList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 28444
 * @date 2018/1/9.
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<MessageBean> {



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageBean message) throws Exception {

        System.out.println(message);
        if (message.getOperationCode() == OperationCode.UPDATE_ROOM_PLAYERS_INFO) {
            BLOKUSRoomPlayerList list=BLOKUSRoomPlayerList.parseFrom(message.getData());
            for (BLOKUSRoomPlayerInfo info : list.getItmesList()) {
                System.out.println(info.getAccount());
                System.out.println(BlokusColor.valueOf(info.getColor()));
                System.out.println(info.getIsReady());
            }
        }
    }

//    优先级高于messageReceived方法，有了这个方法就会屏蔽messageReceived方法
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println(msg.toString());
//    }


}