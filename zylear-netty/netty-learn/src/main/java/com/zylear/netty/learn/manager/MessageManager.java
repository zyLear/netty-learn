package com.zylear.netty.learn.manager;


import com.alibaba.druid.util.StringUtils;
import com.zylear.netty.learn.bean.*;
import com.zylear.netty.learn.cache.ServerCache;
import com.zylear.netty.learn.config.DataSourceBlokusGameConfig;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.domain.GameAccount;
import com.zylear.netty.learn.domain.GameLog;
import com.zylear.netty.learn.domain.PlayerGameLog;
import com.zylear.netty.learn.domain.PlayerGameRecord;
import com.zylear.netty.learn.enums.*;
import com.zylear.netty.learn.service.GameAccountService;
import com.zylear.netty.learn.service.GameLogService;
import com.zylear.netty.learn.service.PlayerGameLogService;
import com.zylear.netty.learn.service.PlayerGameRecordService;
import com.zylear.netty.learn.util.MessageFormater;
import com.zylear.proto.BlokusOuterClass.*;
import io.netty.channel.Channel;
import org.apache.commons.lang3.RandomUtils;
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

    private static final String VERSION = "V1.0.2.RELEASE";

    private GameAccountService gameAccountService;
    private GameLogService gameLogService;
    private PlayerGameLogService playerGameLogService;
    private PlayerGameRecordService playerGameRecordService;

    private static final Integer WIN_CHANGE_SCORE_TEMPLATE = 23;
    private static final Integer LOSE_CHANGE_SCORE_TEMPLATE = -21;
    private static final Integer ESCAPE_CHANGE_SCORE_TEMPLATE = -25;
    private static final Integer SCORE_RANDOM_RANGE = 7; //excluded

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
            case OperationCode.CHAT_IN_ROOM:
                chatInRoom(transferBean, responses);
                break;
            case OperationCode.LEAVE_ROOM_FROM_GAME:
                leaveRoomFromGame(transferBean, responses);
                break;
            case OperationCode.INIT_PLAYER_INFO_IN_GAME:
                initPlayerInfoInGame(transferBean, responses);
                break;
            case OperationCode.LOGOUT:
                logout(transferBean, responses);
                break;

            case OperationCode.QUIT:
                quit(transferBean, responses);
                break;
            default:
        }
    }

    private void logout(TransferBean transferBean, List<TransferBean> responses) {
        quit(transferBean, responses);
    }

    private void initPlayerInfoInGame(TransferBean transferBean, List<TransferBean> responses) {
        RoomInfo roomInfo = ServerCache.getRoomInfo(transferBean.getChannel());
        if (roomInfo != null) {
            Map<String, PlayerRoomInfo> playerRoomInfoMap = roomInfo.getPlayers();
            MessageBean needSendMessage = MessageFormater.formatPlayerInfoInGameMessage(playerRoomInfoMap);
            transferBean.setMessage(needSendMessage);
            responses.add(transferBean);
        }
    }

    private void leaveRoomFromGame(TransferBean transferBean, List<TransferBean> responses) {


    }

    private void chatInRoom(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
        List<Channel> players = ServerCache.getPlayerChannelsInRoom(transferBean.getChannel());
        for (Channel channel : players) {
            responses.add(new TransferBean(message, channel));
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
        List<PlayerGameRecord> playerGameRecords = playerGameRecordService.findRanksByAccount(account.getAccount());
        List<PlayerGameLog> playerGameLogs = playerGameLogService.findByAccount(account.getAccount());
        List<PlayerGameLogViewBean> playerGameLogViewBeans = new LinkedList<>();
        for (PlayerGameLog playerGameLog : playerGameLogs) {
            GameLog gameLog = gameLogService.findById(playerGameLog.getGameLogId());
            List<PlayerGameLog> playerGameLogList = playerGameLogService.findByGameLogId(playerGameLog.getGameLogId());
            PlayerGameLogViewBean playerGameLogViewBean = new PlayerGameLogViewBean();
            playerGameLogViewBean.setGameResult(GameResult.valueOf(playerGameLog.getGameResult()));
            playerGameLogViewBean.setGameType(GameType.valueOf(gameLog.getGameType()));
            playerGameLogViewBean.setStepsCount(playerGameLog.getStepsCount());
            playerGameLogViewBean.setTime(gameLog.getCreateTime());
            StringBuilder detail = new StringBuilder();
            int i = 0;
            for (PlayerGameLog log : playerGameLogList) {
                i++;
                detail.append(log.getAccount()).append(":").append(log.getStepsCount()).append("   ");
                if (i == 2) {
                    detail.append("\n");
                }
            }
            playerGameLogViewBean.setChangeScore(playerGameLog.getChangeScore());
            playerGameLogViewBean.setDetail(detail.toString());
            playerGameLogViewBeans.add(playerGameLogViewBean);
        }
        message = MessageFormater.formatProfileMessage(playerGameRecords, playerGameLogViewBeans);
        responses.add(new TransferBean(message, transferBean.getChannel()));
    }

    private void rankInfo(TransferBean transferBean, List<TransferBean> responses) {

        List<PlayerGameRecord> twoPlayersRanks = playerGameRecordService.findRanks(GameType.blokus_four.getValue());
        List<PlayerGameRecord> fourPlayersRanks = playerGameRecordService.findRanks(GameType.blokus_two.getValue());
        MessageBean message = MessageFormater.formatRankInfoMessage(twoPlayersRanks, fourPlayersRanks);
        responses.add(new TransferBean(message, transferBean.getChannel()));

    }

    @Transactional(value = DataSourceBlokusGameConfig.TX_MANAGER)
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

                PlayerGameRecord gameRecord = new PlayerGameRecord();
                gameRecord.setAccount(account);
                gameRecord.setGameType(GameType.blokus_four.getValue());
                gameRecord.setWinCount(0);
                gameRecord.setLoseCount(0);
                gameRecord.setEscapeCount(0);
                gameRecord.setRankScore(1200);
                gameRecord.setRank(0);
                gameRecord.setCreateTime(date);
                gameRecord.setLastUpdateTime(date);
                playerGameRecordService.insert(gameRecord);
                gameRecord.setGameType(GameType.blokus_two.getValue());
                playerGameRecordService.insert(gameRecord);

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
            gameStatusChange(playerRoomInfo, roomInfo, GameResult.win);
        }
    }

    private synchronized void lose(TransferBean transferBean, List<TransferBean> responses) {
        PlayerRoomInfo playerRoomInfo = ServerCache.getPlayerRoomInfo(transferBean.getChannel());
        RoomInfo roomInfo = ServerCache.getRoomInfo(transferBean.getChannel());
        if (playerRoomInfo != null && roomInfo != null &&
                RoomStatus.gaming.equals(roomInfo.getRoomStatus()) &&
                GameStatus.gaming.equals(playerRoomInfo.getGameStatus())) {
            logger.info("{} lose", playerRoomInfo.getAccount());
            gameStatusChange(playerRoomInfo, roomInfo, GameResult.lose);
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

        lose(transferBean, responses);

        List<Channel> players = ServerCache.getPlayerChannelsInRoom(transferBean.getChannel());
        for (Channel channel : players) {
            responses.add(new TransferBean(message, channel));
        }
    }

    private synchronized void chooseColor(TransferBean transferBean, final List<TransferBean> responses) {

        MessageBean message = transferBean.getMessage();
        BLOKUSColor blokusColor;
        try {
            blokusColor = BLOKUSColor.parseFrom(message.getData());
            logger.info("choose color. color:{}", BlokusColor.valueOf(blokusColor.getColor()));
        } catch (Exception e) {
            logger.warn("parse BLOKUSRoomName exception. ", e);
            return;
        }

        ServerCache.chooseColor(transferBean.getChannel(), BlokusColor.valueOf(blokusColor.getColor()), new EmptyServerCacheCallback() {
            @Override
            public void updateRoomPlayersInfo(String roomName) {
                updateRoomPlayersInfoNotify(roomName, responses);
            }
        });

    }

    private synchronized void quit(TransferBean transferBean, List<TransferBean> responses) {
//        ServerCache.quit(transferBean.getChannel());
        // need modify
        PlayerInfo playerInfo = ServerCache.getPlayerInfo(transferBean.getChannel());

        if (playerInfo != null) {
            RoomInfo roomInfo = playerInfo.getRoomInfo();
            if (roomInfo != null) {
                PlayerRoomInfo playerRoomInfo = roomInfo.getPlayers().get(playerInfo.getAccount());
                int playerCount = roomInfo.getPlayerCount();
                if (playerCount <= 1) {
                    if (RoomStatus.gaming.equals(roomInfo.getRoomStatus()) &&
                            GameStatus.gaming.equals(playerRoomInfo.getGameStatus())) {
                        gameStatusChange(playerRoomInfo, roomInfo, GameResult.win);
                    }
                    ServerCache.removeRoom(roomInfo.getRoomName());
                } else {

                    roomInfo.setPlayerCount(playerCount - 1);
                    roomInfo.getPlayers().remove(playerInfo.getAccount());
                    if (RoomStatus.gaming.equals(roomInfo.getRoomStatus()) &&
                            GameStatus.gaming.equals(playerRoomInfo.getGameStatus())) {
                        MessageBean messageBean = MessageFormater.formatGiveUpMessage(playerRoomInfo.getColor());
                        for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                            // if (!playerInfo.getAccount().equals(entry.getKey())) {
                            responses.add(new TransferBean(messageBean, entry.getValue().getChannel()));
                            //   }
                        }
                        gameStatusChange(playerRoomInfo, roomInfo, GameResult.escape);

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

    @Transactional(value = DataSourceBlokusGameConfig.TX_MANAGER)
    private void gameStatusChange(PlayerRoomInfo playerRoomInfo, RoomInfo roomInfo, GameResult gameResult) {

        Integer changeScore;
        switch (gameResult) {
            case win:
                if (GameType.blokus_four.equals(roomInfo.getGameType())) {
                    changeScore = WIN_CHANGE_SCORE_TEMPLATE * 2 + RandomUtils.nextInt(0, SCORE_RANDOM_RANGE);
                } else {
                    changeScore = WIN_CHANGE_SCORE_TEMPLATE + RandomUtils.nextInt(0, SCORE_RANDOM_RANGE);
                }
                playerRoomInfo.setGameStatus(GameStatus.win);
                playerGameRecordService.update(playerRoomInfo.getAccount(), roomInfo.getGameType().getValue(), 1, 0, 0, changeScore);
                break;
            case lose:
                int currentLoseCount = roomInfo.getCurrentLoseCount();
                if (currentLoseCount == 0) {
                    changeScore = LOSE_CHANGE_SCORE_TEMPLATE - RandomUtils.nextInt(0, SCORE_RANDOM_RANGE);
                } else if (currentLoseCount == 1) {
                    changeScore = -RandomUtils.nextInt(0, SCORE_RANDOM_RANGE);
                } else {
                    changeScore = WIN_CHANGE_SCORE_TEMPLATE + RandomUtils.nextInt(0, SCORE_RANDOM_RANGE);
                }

                playerRoomInfo.setGameStatus(GameStatus.lose);
                roomInfo.setCurrentLoseCount(roomInfo.getCurrentLoseCount() + 1);
                playerGameRecordService.update(playerRoomInfo.getAccount(), roomInfo.getGameType().getValue(), 0, 1, 0, changeScore);
                break;
            case escape:
                changeScore = ESCAPE_CHANGE_SCORE_TEMPLATE - RandomUtils.nextInt(0, SCORE_RANDOM_RANGE);
                roomInfo.setCurrentLoseCount(roomInfo.getCurrentLoseCount() + 1);
                playerGameRecordService.update(playerRoomInfo.getAccount(), roomInfo.getGameType().getValue(), 0, 0, 1, changeScore);
                break;
            default:
                return;
        }
        roomInfo.checkRoomStatus();

        PlayerGameLog playerGameLog = new PlayerGameLog();
        playerGameLog.setAccount(playerRoomInfo.getAccount());
        playerGameLog.setGameLogId(roomInfo.getGameLogId());
        playerGameLog.setGameResult(gameResult.getValue());
        playerGameLog.setStepsCount(playerRoomInfo.getStepsCount());
        playerGameLog.setChangeScore(changeScore);
        playerGameLog.setCreateTime(new Date());
        playerGameLog.setLastUpdateTime(new Date());
        playerGameLogService.insert(playerGameLog);
    }


    private synchronized void chessDone(TransferBean transferBean, List<TransferBean> responses) {

        PlayerRoomInfo playerRoomInfo = ServerCache.getPlayerRoomInfo(transferBean.getChannel());
        if (playerRoomInfo != null) {
            playerRoomInfo.setStepsCount(playerRoomInfo.getStepsCount() + 1);
            List<Channel> channels = ServerCache.getOtherPlayerChannelsInRoom(transferBean.getChannel());
            for (Channel channel : channels) {
                responses.add(new TransferBean(transferBean.getMessage(), channel));
            }
        }


//        return null;
    }


    private synchronized void ready(TransferBean transferBean, final List<TransferBean> responses) {
        ServerCache.ready(transferBean.getChannel(), new EmptyServerCacheCallback() {
            @Override
            public void startGame(RoomInfo roomInfo) {
                if (roomInfo != null) {
                    Date date = new Date();
                    GameLog gameLog = new GameLog();
                    gameLog.setRoomName(roomInfo.getRoomName());
                    gameLog.setGameType(roomInfo.getGameType().getValue());
                    gameLog.setCreateTime(date);
                    gameLog.setLastUpdateTime(date);

                    gameLogService.insert(gameLog);
                    roomInfo.setGameLogId(gameLog.getId());
                    startGameNotify(roomInfo.getRoomName(), responses);
                }
            }

            @Override
            public void updateRoomPlayersInfo(String roomName) {
                updateRoomPlayersInfoNotify(roomName, responses);
            }
        });

    }

    private void startGameNotify(String roomName, List<TransferBean> responses) {
        Map<String, PlayerRoomInfo> playerRoomInfoMap = ServerCache.getPlayerRoomInfos(roomName);

        MessageBean message;
        message = MessageBean.START_BLOKUS_SUCCESS;

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
                MessageBean messageBean = MessageFormater.formatJoinRoomMessage(roomInfo.getRoomName(), roomInfo.getGameType());
                responses.add(new TransferBean(messageBean, transferBean.getChannel()));
                updateRoomPlayersInfoNotify(blokusRoomName.getRoomName(), responses);
            }

//            List<RoomInfo> rooms = ServerCache.getAllRooms();
//            List<Channel> var2 = ServerCache.getPlayersInLobby();

            transferBean.setMessage(MessageBean.JOIN_ROOM_FAIL);
            responses.add(transferBean);
        }
    }


    private void checkVersion(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
        BLOKUSVersion blokusVersion;
        try {
            blokusVersion = BLOKUSVersion.parseFrom(message.getData());
            logger.info("checkVersion. version:{}", blokusVersion.getVersion());
        } catch (Exception e) {
            logger.warn("parse BLOKUSRoomName exception. ", e);
            transferBean.setMessage(MessageBean.CHECK_VERSION_FAIL);
            responses.add(transferBean);
            return;
        }

        if (VERSION.equals(blokusVersion.getVersion())) {
            transferBean.setMessage(MessageBean.CHECK_VERSION_SUCCESS);
            responses.add(transferBean);
        } else {
            transferBean.setMessage(MessageBean.CHECK_VERSION_FAIL);
            responses.add(transferBean);
        }

    }

    private synchronized void createRoom(TransferBean transferBean, List<TransferBean> responses) {
        MessageBean message = transferBean.getMessage();
        BLOKUSCreateRoom blokusCreateRoom = null;
        try {
            blokusCreateRoom = BLOKUSCreateRoom.parseFrom(message.getData());
            logger.info("create room. roomName:{}", blokusCreateRoom.getRoomName());
            logger.info("create room. gameType:{}", GameType.valueOf(blokusCreateRoom.getGameType()));
        } catch (Exception e) {
            logger.warn("parse BLOKUSCreateRoom exception. ", e);
            transferBean.setMessage(MessageBean.CREATE_ROOM_FAIL);
            responses.add(transferBean);
            return;
        }

        if (ServerCache.createRoom(transferBean.getChannel(), blokusCreateRoom.getRoomName(),
                GameType.valueOf(blokusCreateRoom.getGameType()))) {

            transferBean.setMessage(transferBean.getMessage());
            responses.add(transferBean);
            updateRoomPlayersInfoNotify(blokusCreateRoom.getRoomName(), responses);
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
            updateRoomPlayersInfoNotify(roomName, responses);
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


    private void updateRoomPlayersInfoNotify(String roomName, List<TransferBean> responses) {
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
    public void setGameLogService(GameLogService gameLogService) {
        this.gameLogService = gameLogService;
    }

    @Autowired
    public void setPlayerGameLogService(PlayerGameLogService playerGameLogService) {
        this.playerGameLogService = playerGameLogService;
    }

    @Autowired
    public void setPlayerGameRecordService(PlayerGameRecordService playerGameRecordService) {
        this.playerGameRecordService = playerGameRecordService;
    }
}
