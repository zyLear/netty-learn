package com.zylear.netty.learn.manager;


import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.PlayerRoomInfo;
import com.zylear.netty.learn.bean.TransferBean;
import com.zylear.netty.learn.cache.ServerCache;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.enums.RoomType;
import com.zylear.netty.learn.util.MessageFormater;
import com.zylear.proto.BlokusOuterClass.BLOKUSAccount;
import com.zylear.proto.BlokusOuterClass.BLOKUSCreateRoom;
import com.zylear.proto.BlokusOuterClass.BLOKUSRoomName;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Component
public class MessageManager implements MessageHandler<TransferBean, List<TransferBean>> {

    private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);

    // division different function later. different kinds of handlers

    public void handle(TransferBean transferBean, List<TransferBean> responses) {
        System.out.println(transferBean.getMessage().toString());

        switch (transferBean.getMessage().getOperationCode()) {
            case OperationCode.CHECK_VERSION:
                checkVersion(transferBean, responses);
                break;
            case OperationCode.LOGIN:
                login(transferBean, responses);
                break;
            case OperationCode.CREATE_ROOM:
                createRoom(transferBean, responses);
                break;
            case OperationCode.JOIN_ROOM:
                joinRoom(transferBean, responses);
                break;
            case OperationCode.LEAVE_ROOM:
                leaveRoom(transferBean, responses);
                break;
            case OperationCode.CHOOSE_COLOR:
                chooseColor(transferBean, responses);
                break;
            case OperationCode.READY:
                ready(transferBean, responses);
                break;
            case OperationCode.CHESS_DONE:
                chessDone(transferBean, responses);
                break;
            case OperationCode.QUIT:
                quit(transferBean, responses);
                break;
            default:
        }
    }

    private void chooseColor(TransferBean transferBean, List<TransferBean> responses) {

        MessageBean message = transferBean.getMessage();


    }

    private void quit(TransferBean transferBean, List<TransferBean> responses) {
        ServerCache.quit(transferBean.getChannel());
//        return Collections.EMPTY_LIST;
    }

    private void chessDone(TransferBean transferBean, List<TransferBean> responses) {

//        return null;


    }


    private synchronized List<TransferBean> ready(TransferBean transferBean, List<TransferBean> responses) {

        ServerCache.ready(transferBean.getChannel());
        return Collections.EMPTY_LIST;
    }


    private void joinRoom(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
        BLOKUSRoomName blokusRoomName;
        try {
            blokusRoomName = BLOKUSRoomName.parseFrom(message.getData());
            logger.info("join room. roomName:{}", blokusRoomName.getRoomName());
        } catch (Exception e) {
            logger.warn("parse BLOKUSRoomName exception. ", e);
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
            responses.add(transferBean);
            return;
        }

        if (ServerCache.joinRoom(transferBean.getChannel(), blokusRoomName.getRoomName())) {
            responses.add(new TransferBean(MessageBean.JOIN_ROOM_SUCCESS, transferBean.getChannel()));
            updateRoomPlayersInfo(blokusRoomName.getRoomName(), responses);
//            List<RoomInfo> rooms = ServerCache.getAllRooms();
//            List<Channel> var2 = ServerCache.getPlayersInLobby();
        } else {
            transferBean.setMessage(MessageBean.JOIN_ROOM_FAIL);
            responses.add(transferBean);
        }

    }


    private void checkVersion(TransferBean transferBean, List<TransferBean> responses) {


//        return null;
    }

    private void createRoom(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
        BLOKUSCreateRoom blokusCreateRoom = null;
        try {
            blokusCreateRoom = BLOKUSCreateRoom.parseFrom(message.getData());
            logger.info("create room. roomName:{}", blokusCreateRoom.getRoomName());
            logger.info("create room. roomType:{}", RoomType.valueOf(blokusCreateRoom.getRoomType()));
        } catch (Exception e) {
            logger.warn("parse BLOKUSCreateRoom exception. ", e);
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
            responses.add(transferBean);
            return;
        }

        if (ServerCache.createRoom(transferBean.getChannel(), blokusCreateRoom.getRoomName(),
                RoomType.valueOf(blokusCreateRoom.getRoomType()))) {
            transferBean.setMessage(MessageBean.CREATE_ROOM_SUCCESS);
            responses.add(transferBean);
            updateRoomPlayersInfo(blokusCreateRoom.getRoomName(), responses);
        } else {
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
            responses.add(transferBean);
        }
//        ServerCache.showAllRooms();

    }


    private void leaveRoom(TransferBean transferBean, List<TransferBean> responses) {
        String roomName = ServerCache.leaveRoom(transferBean.getChannel());
        if (roomName != null) {
            transferBean.setMessage(MessageBean.LEAVE_ROOM_SUCCESS);
            responses.add(transferBean);
            updateRoomPlayersInfo(roomName, responses);
        } else {
            transferBean.setMessage(MessageBean.LEAVE_ROOM_FAIL);
        }
//        ServerCache.showAllRooms();
        responses.add(transferBean);
    }


    private void login(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
        BLOKUSAccount account;
        try {
            account = BLOKUSAccount.parseFrom(message.getData());
            logger.info("login. account:{}", account.getAccount());
            logger.info("login. password:{}", account.getPassword());
        } catch (Exception e) {
            logger.warn("parse BLOKUSAccount exception. ", e);
            transferBean.setMessage(MessageBean.LOGIN_FAIL);
            responses.add(transferBean);
            return;
        }

        if (("123456".equals(account.getAccount())) || "654321".equals(account.getAccount()) &&
                "123456".equals(account.getPassword())) {
            if (ServerCache.login(transferBean.getChannel(), account.getAccount())) {
                transferBean.setMessage(MessageBean.LOGIN_SUCCESS);
                responses.add(transferBean);
                return;
            }
        }
        transferBean.setMessage(MessageBean.LOGIN_FAIL);
        responses.add(transferBean);
    }

    private void roomStatusChange(Channel channel) {

//        return null;
    }


    private void updateRoomPlayersInfo(String roomName, List<TransferBean> transferBeans) {
        Map<String, PlayerRoomInfo> playerRoomInfoMap = ServerCache.getPlayerRoomInfos(roomName);
        MessageBean needSendMessage = MessageFormater.formatPlayerRoomInfoMessage(playerRoomInfoMap);
        for (Entry<String, PlayerRoomInfo> entry : playerRoomInfoMap.entrySet()) {
            transferBeans.add(new TransferBean(needSendMessage, entry.getValue().getChannel()));
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
