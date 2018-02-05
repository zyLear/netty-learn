package com.zylear.netty.learn.manager;


import com.google.protobuf.InvalidProtocolBufferException;
import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.PlayerInfo;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.bean.TransferBean;
import com.zylear.netty.learn.cache.ServerCache;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;
import com.zylear.proto.BlokusOuterClass.BLOKUSAccount;
import com.zylear.proto.BlokusOuterClass.BLOKUSCreateRoom;
import com.zylear.proto.BlokusOuterClass.BLOKUSRoomName;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Component
public class MessageManager implements MessageHandler<TransferBean, List<TransferBean>> {

    private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);

    // division different function later. different kinds of handlers

    public List<TransferBean> handle(TransferBean transferBean) {
        System.out.println(transferBean.getMessage().toString());

        switch (transferBean.getMessage().getOperationCode()) {
            case OperationCode.CHECK_VERSION:
                return checkVersion(transferBean);
            case OperationCode.LOGIN:
                return login(transferBean);
            case OperationCode.CREATE_ROOM:
                return createRoom(transferBean);
            case OperationCode.JOIN_ROOM:
                return joinRoom(transferBean);
            case OperationCode.LEAVE_ROOM:
                return leaveRoom(transferBean);
            case OperationCode.READY:
                return ready(transferBean);
            case OperationCode.CHESS_DONE:
                return chessDone(transferBean);
            default:
                return Collections.EMPTY_LIST;
        }
    }

    private List<TransferBean> chessDone(TransferBean transferBean) {

        return null;


    }


    private synchronized List<TransferBean> ready(TransferBean transferBean) {

        ServerCache.ready(transferBean.getChannel());
        return Collections.EMPTY_LIST;
    }


    private List<TransferBean> joinRoom(TransferBean transferBean) {
        MessageBean message = transferBean.getMessage();
        BLOKUSRoomName blokusRoomName;
        try {
            blokusRoomName = BLOKUSRoomName.parseFrom(message.getData());
            logger.info("join room. roomName:{}", blokusRoomName.getRoomName());
        } catch (Exception e) {
            logger.warn("parse BLOKUSRoomName exception. ", e);
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
            return Arrays.asList(transferBean);
        }

        if (ServerCache.joinRoom(transferBean.getChannel(), blokusRoomName.getRoomName())) {
            transferBean.setMessage(MessageBean.JOIN_ROOM_SUCCESS);
        } else {
            transferBean.setMessage(MessageBean.JOIN_ROOM_FAIL);
        }
        return Arrays.asList(transferBean);
    }

    private List<TransferBean> checkVersion(TransferBean transferBean) {


        return null;
    }

    private List<TransferBean> createRoom(TransferBean transferBean) {
        MessageBean message = transferBean.getMessage();
        BLOKUSCreateRoom blokusCreateRoom = null;
        try {
            blokusCreateRoom = BLOKUSCreateRoom.parseFrom(message.getData());
            logger.info("create room. roomName:{}", blokusCreateRoom.getRoomName());
            logger.info("create room. roomType:{}", RoomType.valueOf(blokusCreateRoom.getRoomType()));
        } catch (Exception e) {
            logger.warn("parse BLOKUSCreateRoom exception. ", e);
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
            return Arrays.asList(transferBean);
        }

        if (ServerCache.createRoom(transferBean.getChannel(), blokusCreateRoom.getRoomName(),
                RoomType.valueOf(blokusCreateRoom.getRoomType()))) {
            transferBean.setMessage(MessageBean.CREATE_ROOM_SUCCESS);
        } else {
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
        }
        return Arrays.asList(transferBean);
    }


    private List<TransferBean> leaveRoom(TransferBean transferBean) {
//        MessageBean message = transferBean.getMessage();
//        BLOKUSRoomName blokusRoomName;
//        try {
//            blokusRoomName = BLOKUSRoomName.parseFrom(message.getData());
//            logger.info("join room. roomName:{}", blokusRoomName.getRoomName());
//        } catch (Exception e) {
//            logger.warn("parse BLOKUSRoomName exception. ", e);
//            transferBean.setMessage(MessageBean.LEAVE_ROOM_FAIL);
//            return Arrays.asList(transferBean);
//        }

        if (ServerCache.leaveRoom(transferBean.getChannel())) {
            transferBean.setMessage(MessageBean.LEAVE_ROOM_SUCCESS);
        } else {
            transferBean.setMessage(MessageBean.LEAVE_ROOM_FAIL);
        }
        return Arrays.asList(transferBean);
    }


    private List<TransferBean> login(TransferBean transferBean) {
        MessageBean message = transferBean.getMessage();
        BLOKUSAccount account;
        try {
            account = BLOKUSAccount.parseFrom(message.getData());
            logger.info("login. account:{}", account.getAccount());
            logger.info("login. password:{}", account.getPassword());
        } catch (Exception e) {
            logger.warn("parse BLOKUSAccount exception. ", e);
            transferBean.setMessage(MessageBean.LOGIN_FAIL);
            return Arrays.asList(transferBean);
        }

        if ("123456".equals(account.getAccount()) && "123456".equals(account.getPassword())) {
            ServerCache.login(transferBean.getChannel(), account.getAccount());
            transferBean.setMessage(MessageBean.LOGIN_SUCCESS);
        } else {
            transferBean.setMessage(MessageBean.LOGIN_FAIL);
        }
        return Arrays.asList(transferBean);
    }


    private List<TransferBean> roomChange() {


        return null;
    }


    public void send(List<TransferBean> transferBeans) {
        if (transferBeans != null) {
            for (TransferBean transferBean : transferBeans) {
                transferBean.getChannel().writeAndFlush(transferBean.getMessage());
            }
        }
    }

}
