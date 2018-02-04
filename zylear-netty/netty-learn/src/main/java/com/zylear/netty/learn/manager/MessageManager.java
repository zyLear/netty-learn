package com.zylear.netty.learn.manager;


import com.google.protobuf.InvalidProtocolBufferException;
import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.bean.TransferBean;
import com.zylear.netty.learn.cache.ServerCache;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;
import com.zylear.proto.BlokusOuterClass.BLOKUSAccount;
import com.zylear.proto.BlokusOuterClass.BLOKUSCreateRoom;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Component
public class MessageManager implements MessageHandler<TransferBean, List<TransferBean>> {

    // division different function later

    public List<TransferBean> handle(TransferBean transferBean) {
        System.out.println(transferBean.getMessage().toString());

        switch (transferBean.getMessage().getOperationCode()) {
            case OperationCode.CHECK_VERSION:
                return checkVersion(transferBean);
            case OperationCode.LOGIN:
                return login(transferBean);
            case OperationCode.CREATE_ROOM:
                return createRoom(transferBean);
            default:
                return Collections.EMPTY_LIST;
        }
    }

    private List<TransferBean> checkVersion(TransferBean transferBean) {
        return null;
    }

    private List<TransferBean> createRoom(TransferBean transferBean) {
        MessageBean message = transferBean.getMessage();
        BLOKUSCreateRoom blokusCreateRoom = null;
        try {
            blokusCreateRoom = BLOKUSCreateRoom.parseFrom(message.getData());
            System.out.println(blokusCreateRoom.getRoomName());
            System.out.println(blokusCreateRoom.getRoomType());
        } catch (Exception e) {
            return Arrays.asList(new TransferBean(MessageBean.CREATE_ROOM_FAIL, transferBean.getChannel()));
        }

        if (ServerCache.createRoom(transferBean.getChannel(), blokusCreateRoom.getRoomName(),
                RoomType.valueOf(blokusCreateRoom.getRoomType()))) {
            return Arrays.asList(new TransferBean(MessageBean.CREATE_ROOM_SUCCESS, transferBean.getChannel()));

        } else {
            return Arrays.asList(new TransferBean(MessageBean.CREATE_ROOM_FAIL, transferBean.getChannel()));
        }
    }

    private List<TransferBean> login(TransferBean transferBean) {
        MessageBean message = transferBean.getMessage();
        BLOKUSAccount account;
        try {
            account = BLOKUSAccount.parseFrom(message.getData());
            System.out.println(account.getAccount());
            System.out.println(account.getPassword());
        } catch (Exception e) {
            return Arrays.asList(new TransferBean(MessageBean.LOGIN_FAIL, transferBean.getChannel()));
        }

        if ("123456".equals(account.getAccount()) && "123456".equals(account.getPassword())) {
            ServerCache.login(transferBean.getChannel(), account.getAccount());
            return Arrays.asList(new TransferBean(MessageBean.LOGIN_SUCCESS, transferBean.getChannel()));
        } else {
            return Arrays.asList(new TransferBean(MessageBean.LOGIN_FAIL, transferBean.getChannel()));
        }

    }

    public void send(List<TransferBean> transferBeans) {
        if (transferBeans != null) {
            for (TransferBean transferBean : transferBeans) {
                transferBean.getChannel().writeAndFlush(transferBean.getMessage());
            }
        }
    }

}
