package com.zylear.netty.learn.manager;


import com.alibaba.druid.util.StringUtils;
import com.sun.imageio.spi.RAFImageInputStreamSpi;
import com.zylear.netty.learn.bean.*;
import com.zylear.netty.learn.cache.ServerCache;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.domain.GameAccount;
import com.zylear.netty.learn.domain.GameRecord;
import com.zylear.netty.learn.domain.GameRecordDetail;
import com.zylear.netty.learn.enums.*;
import com.zylear.netty.learn.service.GameAccountService;
import com.zylear.netty.learn.service.GameRecordDetailService;
import com.zylear.netty.learn.service.GameRecordService;
import com.zylear.netty.learn.util.MessageFormater;
import com.zylear.proto.BlokusOuterClass.*;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Component
public class MessageManager implements MessageHandler<TransferBean, List<TransferBean>> {

    private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);

    private GameAccountService gameAccountService;
    private GameRecordService gameRecordService;
    private GameRecordDetailService gameRecordDetailService;

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
            case OperationCode.LOSE:
                lose(transferBean, responses);
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
            case OperationCode.REGISTER:
                register(transferBean, responses);
                break;
            case OperationCode.RANK_INFO:
                rankInfo(transferBean, responses);
                break;
            case OperationCode.PROFILE:
                profile(transferBean, responses);
                break;


            case OperationCode.QUIT:
                quit(transferBean, responses);
                break;
            default:
        }
    }

    private void profile(TransferBean transferBean, List<TransferBean> responses) {

        MessageBean message = transferBean.getMessage();
        BLOKUSAccount account;
        try {
            account = BLOKUSAccount.parseFrom(message.getData());
            logger.info("profile. account:{}", account.getAccount());
        } catch (Exception e) {
            logger.warn("parse BLOKUSAccount exception. ", e);
            return;
        }
        List<GameRecord> gameRecord = gameRecordService.findRanksByAccount(account.getAccount());
        message = MessageFormater.formatProfileMessage(gameRecord);
        responses.add(new TransferBean(message, transferBean.getChannel()));
    }

    private void rankInfo(TransferBean transferBean, List<TransferBean> responses) {

        List<GameRecord> twoPlayersRanks = gameRecordService.findRanks(RoomType.blokus_four.getValue());
        List<GameRecord> fourPlayersRanks = gameRecordService.findRanks(RoomType.blokus_two.getValue());
        MessageBean message = MessageFormater.formatRankInfoMessage(twoPlayersRanks, fourPlayersRanks);
        responses.add(new TransferBean(message, transferBean.getChannel()));

    }

    @Transactional
    private synchronized void register(TransferBean transferBean, List<TransferBean> responses) {

        MessageBean message = transferBean.getMessage();
        BLOKUSGameAccount blokusGameAccount;
        try {
            blokusGameAccount = BLOKUSGameAccount.parseFrom(message.getData());
            logger.info("register. account:{}", blokusGameAccount.getAccount());
            logger.info("register. roomName:{}", blokusGameAccount.getPassword());
        } catch (Exception e) {
            logger.warn("parse BLOKUSGameAccount exception. ", e);
            responses.add(new TransferBean(MessageBean.REGISTER_FAIL, transferBean.getChannel()));
            return;
        }
        String account = blokusGameAccount.getAccount();
        String password = blokusGameAccount.getPassword();

        try {
            GameAccount gameAccount = gameAccountService.findByAccount(account);
            if (gameAccount == null) {
                Date date = new Date();
                gameAccount = new GameAccount();
                gameAccount.setAccount(account);
                gameAccount.setPassword(password);
                gameAccount.setStars(0);
                gameAccount.setCreateTime(date);
                gameAccount.setLastUpdateTime(date);
                gameAccountService.insert(gameAccount);

                GameRecord gameRecord = new GameRecord();
                gameRecord.setAccount(account);
                gameRecord.setGameType(RoomType.blokus_four.getValue());
                gameRecord.setWinCount(0);
                gameRecord.setLoseCount(0);
                gameRecord.setEscapeCount(0);
                gameRecord.setRankScore(12000);
                gameRecord.setRank(0);
                gameRecord.setCreateTime(date);
                gameRecord.setLastUpdateTime(date);
                gameRecordService.insert(gameRecord);
                gameRecord.setGameType(RoomType.blokus_two.getValue());
                gameRecordService.insert(gameRecord);

                responses.add(new TransferBean(MessageBean.REGISTER_SUCCESS, transferBean.getChannel()));
            }
        } catch (Exception e) {
            responses.add(new TransferBean(MessageBean.REGISTER_FAIL, transferBean.getChannel()));
            logger.info("register lose. ", e);
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

    @Transactional
    private synchronized void win(TransferBean transferBean, List<TransferBean> responses) {
        PlayerRoomInfo playerRoomInfo = ServerCache.getPlayerRoomInfo(transferBean.getChannel());
        RoomInfo roomInfo = ServerCache.getRoomInfo(transferBean.getChannel());
        if (playerRoomInfo != null && roomInfo != null &&
                RoomStatus.gaming.equals(roomInfo.getRoomStatus()) &&
                !GameStatus.win.equals(playerRoomInfo.getGameStatus())) {
            logger.info("{} win", playerRoomInfo.getAccount());
            playerRoomInfo.setGameStatus(GameStatus.win);
            roomInfo.setRoomStatus(RoomStatus.waiting);

            gameRecordService.update(playerRoomInfo.getAccount(), roomInfo.getRoomType().getValue(), 1, 0, 0, 25);

            GameRecordDetail gameRecordDetail = new GameRecordDetail();
            gameRecordDetail.setAccount(playerRoomInfo.getAccount());
            gameRecordDetail.setRoomName(roomInfo.getRoomName());
            gameRecordDetail.setRoomMembers(roomInfo.getPlayers().values().toString());
            gameRecordDetail.setGameType(roomInfo.getRoomType().getValue());
            gameRecordDetail.setGameResult(GameResult.win.getValue());
            gameRecordDetail.setCreateTime(new Date());
            gameRecordDetail.setLastUpdateTime(new Date());
            gameRecordDetailService.insert(gameRecordDetail);

        }
    }

    private synchronized void lose(TransferBean transferBean, List<TransferBean> responses) {
        PlayerRoomInfo playerRoomInfo = ServerCache.getPlayerRoomInfo(transferBean.getChannel());
        RoomInfo roomInfo = ServerCache.getRoomInfo(transferBean.getChannel());
        if (playerRoomInfo != null && roomInfo != null &&
                RoomStatus.gaming.equals(roomInfo.getRoomStatus()) &&
                GameStatus.gaming.equals(playerRoomInfo.getGameStatus())) {
            logger.info("{} lose", playerRoomInfo.getAccount());
            playerRoomInfo.setGameStatus(GameStatus.fail);


            gameRecordService.update(playerRoomInfo.getAccount(), roomInfo.getRoomType().getValue(), 0, 1, 0, -25);

            GameRecordDetail gameRecordDetail = new GameRecordDetail();
            gameRecordDetail.setAccount(playerRoomInfo.getAccount());
            gameRecordDetail.setRoomName(roomInfo.getRoomName());
            gameRecordDetail.setRoomMembers(roomInfo.getPlayers().values().toString());
            gameRecordDetail.setGameType(roomInfo.getRoomType().getValue());
            gameRecordDetail.setGameResult(GameResult.lose.getValue());
            gameRecordDetail.setCreateTime(new Date());
            gameRecordDetail.setLastUpdateTime(new Date());
            gameRecordDetailService.insert(gameRecordDetail);


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
                        MessageBean messageBean = MessageFormater.formatGiveUpMessage(playerRoomInfo.getColor());
                        for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                            if (!playerInfo.getAccount().equals(entry.getKey())) {
                                responses.add(new TransferBean(messageBean, entry.getValue().getChannel()));
                            }
                        }

                        //change db

                        gameRecordService.update(playerRoomInfo.getAccount(), roomInfo.getRoomType().getValue(), 0, 0, 1, -25);

                        GameRecordDetail gameRecordDetail = new GameRecordDetail();
                        gameRecordDetail.setAccount(playerRoomInfo.getAccount());
                        gameRecordDetail.setRoomName(roomInfo.getRoomName());
                        gameRecordDetail.setRoomMembers(roomInfo.getPlayers().values().toString());
                        gameRecordDetail.setGameType(roomInfo.getRoomType().getValue());
                        gameRecordDetail.setGameResult(GameResult.escape.getValue());
                        gameRecordDetail.setCreateTime(new Date());
                        gameRecordDetail.setLastUpdateTime(new Date());
                        gameRecordDetailService.insert(gameRecordDetail);


                    } else if (RoomStatus.waiting.equals(roomInfo.getRoomStatus())) {
                        MessageBean needSendMessage = MessageFormater.formatPlayerRoomInfoMessage(roomInfo.getPlayers());
                        for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                            responses.add(new TransferBean(needSendMessage, entry.getValue().getChannel()));
                        }
                    }
                }
            }
            ServerCache.removePlayer(transferBean.getChannel());

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

        GameAccount gameAccount =
                gameAccountService.findByAccountAndPassowrd(account.getAccount(), account.getPassword());
        if (gameAccount != null && ServerCache.login(transferBean.getChannel(), account.getAccount())) {
            responses.add(transferBean);
        } else {
            transferBean.setMessage(MessageBean.LOGIN_FAIL);
            responses.add(transferBean);
            return;
        }

        if (!StringUtils.isEmpty(account.getAccount())) {
            if (ServerCache.login(transferBean.getChannel(), account.getAccount())) {

                responses.add(transferBean);
                return;
            }
        }

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

    @Autowired
    public void setGameAccountService(GameAccountService gameAccountService) {
        this.gameAccountService = gameAccountService;
    }

    @Autowired
    public void setGameRecordService(GameRecordService gameRecordService) {
        this.gameRecordService = gameRecordService;
    }

    @Autowired
    public void setGameRecordDetailService(GameRecordDetailService gameRecordDetailService) {
        this.gameRecordDetailService = gameRecordDetailService;
    }
}
