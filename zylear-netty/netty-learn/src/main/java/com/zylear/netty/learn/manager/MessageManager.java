package com.zylear.netty.learn.manager;


import com.alibaba.druid.util.StringUtils;
import com.zylear.netty.learn.bean.*;
import com.zylear.netty.learn.cache.ServerCache;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.enums.ChooseColor;
import com.zylear.netty.learn.enums.GameStatus;
import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;
import com.zylear.netty.learn.util.MessageFormater;
import com.zylear.proto.BlokusOuterClass.*;
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
            case OperationCode.GIVE_UP:
                giveUp(transferBean, responses);
                break;
            case OperationCode.FAIL:
                fail(transferBean, responses);
                break;
            case OperationCode.WIN:
                win(transferBean, responses);
                break;
            case OperationCode.CHAT_IN_GAME:
                chatInGame(transferBean, responses);
                break;
            case OperationCode.ROOM_LIST:
                roomList(transferBean, responses);
                break;


            case OperationCode.QUIT:
                quit(transferBean, responses);
                break;
            default:
        }
    }

    private void roomList(TransferBean transferBean, List<TransferBean> responses) {
        Collection<RoomInfo> roomList = ServerCache.roomList();
        MessageBean message = MessageFormater.formatRoomListMessage(roomList);
        responses.add(new TransferBean(message, transferBean.getChannel()));

    }

    private void chatInGame(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
//        BLOKUSChatMessage blokusChatMessage;
//        try {
//            blokusChatMessage = BLOKUSChatMessage.parseFrom(message.getData());
//            logger.info("chatInGame. message:{}", blokusChatMessage.getChatMessage());
//        } catch (Exception e) {
//            logger.warn("parse BLOKUSChatMessage exception. ", e);
//            return;
//        }

        List<Channel> players = ServerCache.getPlayerChannelsInRoom(transferBean.getChannel());
        for (Channel channel : players) {
            responses.add(new TransferBean(message, channel));
        }
    }

    private synchronized void win(TransferBean transferBean, List<TransferBean> responses) {
        PlayerRoomInfo playerRoomInfo = ServerCache.getPlayerRoomInfo(transferBean.getChannel());
        RoomInfo roomInfo = ServerCache.getRoomInfo(transferBean.getChannel());
        if (playerRoomInfo != null && roomInfo != null &&
                RoomStatus.gaming.equals(roomInfo.getRoomStatus()) &&
                !GameStatus.win.equals(playerRoomInfo.getGameStatus())) {
            logger.info("{} win", playerRoomInfo.getAccount());
            playerRoomInfo.setGameStatus(GameStatus.win);

                roomInfo.setRoomStatus(RoomStatus.waiting);

            // todo db
        }
    }

    private synchronized void fail(TransferBean transferBean, List<TransferBean> responses) {
        PlayerRoomInfo playerRoomInfo = ServerCache.getPlayerRoomInfo(transferBean.getChannel());
        if (playerRoomInfo != null && !GameStatus.fail.equals(playerRoomInfo.getGameStatus())) {
            logger.info("{} fail", playerRoomInfo.getAccount());
            playerRoomInfo.setGameStatus(GameStatus.fail);
            // todo db
        }
    }

    private void giveUp(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
//        BLOKUSChooseColor blokusChooseColor;
//        try {
//            blokusChooseColor = BLOKUSChooseColor.parseFrom(message.getData());
//            logger.info("giveUp. color:{}", blokusChooseColor.getAccount());
//        } catch (Exception e) {
//            logger.warn("parse BLOKUSChooseColor exception. ", e);
//            return;
//        }

        List<Channel> players = ServerCache.getPlayerChannelsInRoom(transferBean.getChannel());
        for (Channel channel : players) {
            responses.add(new TransferBean(message, channel));
        }
    }

    private synchronized void chooseColor(TransferBean transferBean, List<TransferBean> responses) {

        MessageBean message = transferBean.getMessage();
        BLOKUSChooseColor blokusChooseColor;
        try {
            blokusChooseColor = BLOKUSChooseColor.parseFrom(message.getData());
            logger.info("choose color. account:{}", blokusChooseColor.getAccount());
            logger.info("choose color. roomName:{}", blokusChooseColor.getRoomName());
            logger.info("choose color. color:{}", ChooseColor.valueOf(blokusChooseColor.getColor()));
        } catch (Exception e) {
            logger.warn("parse BLOKUSRoomName exception. ", e);
//            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
//            responses.add(transferBean);
            return;
        }

        ServerCache.chooseColor(blokusChooseColor.getAccount(), blokusChooseColor.getRoomName(),
                ChooseColor.valueOf(blokusChooseColor.getColor()));
        updateRoomPlayersInfo(blokusChooseColor.getRoomName(), responses);
    }

    private synchronized void quit(TransferBean transferBean, List<TransferBean> responses) {
//        ServerCache.quit(transferBean.getChannel());
        // need modify
        PlayerInfo playerInfo = ServerCache.getPlayerInfo(transferBean.getChannel());
        if (playerInfo != null) {
            RoomInfo roomInfo = playerInfo.getRoomInfo();
            if (roomInfo != null) {
                int playerCount = roomInfo.getPlayerCount();
                if (playerCount <= 1) {
                    ServerCache.removeRoom(roomInfo.getRoomName());
                } else {
                    PlayerRoomInfo playerRoomInfo = roomInfo.getPlayers().get(playerInfo.getAccount());
                    roomInfo.setPlayerCount(playerCount - 1);
                    roomInfo.getPlayers().remove(playerInfo.getAccount());
                    if (RoomStatus.gaming.equals(roomInfo.getRoomStatus())) {
                        for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {

                            MessageBean messageBean = MessageFormater.formatGiveUpMessage(playerRoomInfo.getColor());

                            responses.add(new TransferBean(messageBean, entry.getValue().getChannel()));
                        }
                    } else if (RoomStatus.waiting.equals(roomInfo.getRoomStatus())) {
                        MessageBean needSendMessage = MessageFormater.formatPlayerRoomInfoMessage(roomInfo.getPlayers());
                        for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                            responses.add(new TransferBean(needSendMessage, entry.getValue().getChannel()));
                        }
                    }
                }
            }
            ServerCache.removePlayer(transferBean.getChannel());

            //todo db score
        }
    }

    private void chessDone(TransferBean transferBean, List<TransferBean> responses) {

        List<Channel> channels = ServerCache.getOtherPlayerChannelsInRoom(transferBean.getChannel());
        for (Channel channel : channels) {
            responses.add(new TransferBean(transferBean.getMessage(), channel));
        }
//        return null;
    }


    private synchronized void ready(TransferBean transferBean, List<TransferBean> responses) {

//        MessageBean message = transferBean.getMessage();
//        BLOKUSChooseColor blokusChooseColor;
//        try {
//            blokusChooseColor = BLOKUSChooseColor.parseFrom(message.getData());
//            logger.info("choose color. account:{}", blokusChooseColor.getAccount());
//            logger.info("choose color. roomName:{}", blokusChooseColor.getRoomName());
//        } catch (Exception e) {
//            logger.warn("parse BLOKUSRoomName exception. ", e);
//            return;
//        }

        PlayerInfo player = ServerCache.getPlayerInfo(transferBean.getChannel());
        if (player == null) {
            return;
        }


        int result = ServerCache.ready(player.getAccount(), player.getRoomName());
        switch (result) {
            case 0:

                startGame(player.getRoomName(), responses);
                break;
            case 1:

                updateRoomPlayersInfo(player.getRoomName(), responses);
                break;
            default:
        }


//        updateRoomPlayersInfo(blokusChooseColor.getRoomName(), responses);
    }

    private void startGame(String roomName, List<TransferBean> responses) {
        Map<String, PlayerRoomInfo> playerRoomInfoMap = ServerCache.getPlayerRoomInfos(roomName);
//        MessageBean needSendMessage = MessageFormater.formatPlayerRoomInfoMessage(playerRoomInfoMap);

//        RoomInfo roomInfo = ServerCache.getRoomInfo(roomName);
//        if (roomInfo != null) {
        MessageBean message;
//            if (RoomType.blokus_four.equals(roomInfo.getRoomType())) {
        message = MessageBean.START_BLOKUS;
//            } else if (RoomType.blokus_two.equals(roomInfo.getRoomType())) {
//                message = MessageBean.START_BLOKUS_TWO_PEOPLE;
//            } else {
//                return;
//            }


        for (Entry<String, PlayerRoomInfo> entry : playerRoomInfoMap.entrySet()) {
            responses.add(new TransferBean(message, entry.getValue().getChannel()));
        }
//        }
    }


    private synchronized void joinRoom(TransferBean transferBean, List<TransferBean> responses) {
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

            RoomInfo roomInfo = ServerCache.getRoomInfo(blokusRoomName.getRoomName());
            if (roomInfo != null) {
                MessageBean messageBean = MessageFormater.formatJoinRoomMessage(roomInfo.getRoomName(), roomInfo.getRoomType());
                responses.add(new TransferBean(messageBean, transferBean.getChannel()));
                updateRoomPlayersInfo(blokusRoomName.getRoomName(), responses);
            }

//            List<RoomInfo> rooms = ServerCache.getAllRooms();
//            List<Channel> var2 = ServerCache.getPlayersInLobby();

            transferBean.setMessage(MessageBean.JOIN_ROOM_FAIL);
            responses.add(transferBean);
        }
    }


    private void checkVersion(TransferBean transferBean, List<TransferBean> responses) {


//        return null;
    }

    private synchronized void createRoom(TransferBean transferBean, List<TransferBean> responses) {
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

            transferBean.setMessage(transferBean.getMessage());
            responses.add(transferBean);
            updateRoomPlayersInfo(blokusCreateRoom.getRoomName(), responses);
        } else {
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
            responses.add(transferBean);
        }
//        ServerCache.showAllRooms();

    }


    private synchronized void leaveRoom(TransferBean transferBean, List<TransferBean> responses) {
        String roomName = ServerCache.leaveRoom(transferBean.getChannel());
        if (roomName != null) {
            transferBean.setMessage(MessageBean.LEAVE_ROOM_SUCCESS);
            responses.add(transferBean);
            updateRoomPlayersInfo(roomName, responses);
        } else {
            transferBean.setMessage(MessageBean.LEAVE_ROOM_FAIL);
            responses.add(transferBean);
        }
//        ServerCache.showAllRooms();

    }


    private synchronized void login(TransferBean transferBean, List<TransferBean> responses) {
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


//        if (("123456".equals(account.getAccount())) ||
//                "654321".equals(account.getAccount()) ||
//                account.getAccount().length() > 5 &&
//                        "123456".equals(account.getPassword())) {
        if (!StringUtils.isEmpty(account.getAccount())) {
            if (ServerCache.login(transferBean.getChannel(), account.getAccount())) {
//                transferBean.setMessage(message);
                responses.add(transferBean);
                return;
            }
        }
//        }
        transferBean.setMessage(MessageBean.LOGIN_FAIL);
        responses.add(transferBean);
    }

    private void roomStatusChange(Channel channel) {

//        return null;
    }


    private void updateRoomPlayersInfo(String roomName, List<TransferBean> responses) {
        Map<String, PlayerRoomInfo> playerRoomInfoMap = ServerCache.getPlayerRoomInfos(roomName);
        MessageBean needSendMessage = MessageFormater.formatPlayerRoomInfoMessage(playerRoomInfoMap);
        for (Entry<String, PlayerRoomInfo> entry : playerRoomInfoMap.entrySet()) {
            responses.add(new TransferBean(needSendMessage, entry.getValue().getChannel()));
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
