package com.zylear.netty.learn.util;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.PlayerGameLogViewBean;
import com.zylear.netty.learn.bean.PlayerRoomInfo;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import com.zylear.netty.learn.domain.PlayerGameRecord;
import com.zylear.netty.learn.enums.BlokusColor;
import com.zylear.netty.learn.enums.GameType;
import com.zylear.proto.BlokusOuterClass.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by xiezongyu on 2018/2/6.
 */
public class MessageFormater {

    public static MessageBean formatPlayerRoomInfoMessage(Map<String, PlayerRoomInfo> playerRoomInfoMap) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.UPDATE_ROOM_PLAYERS_INFO);
        message.setStatusCode(StatusCode.SUCCESS);
        BLOKUSRoomPlayerList.Builder builder = BLOKUSRoomPlayerList.newBuilder();
        for (Entry<String, PlayerRoomInfo> entry : playerRoomInfoMap.entrySet()) {
            PlayerRoomInfo playerRoomInfo = entry.getValue();
            BLOKUSRoomPlayerInfo.Builder itemBuilder = BLOKUSRoomPlayerInfo.newBuilder();
            itemBuilder.setAccount(playerRoomInfo.getAccount());
            itemBuilder.setColor(playerRoomInfo.getColor().getValue());
            itemBuilder.setIsReady(playerRoomInfo.getReady());
            builder.addItmes(itemBuilder);
        }
        message.setData(builder.build().toByteArray());
        return message;
    }

    public static MessageBean formatPlayerInfoInGameMessage(Map<String, PlayerRoomInfo> playerRoomInfoMap) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.INIT_PLAYER_INFO_IN_GAME);
        message.setStatusCode(StatusCode.SUCCESS);
        BLOKUSRoomPlayerList.Builder builder = BLOKUSRoomPlayerList.newBuilder();
        for (Entry<String, PlayerRoomInfo> entry : playerRoomInfoMap.entrySet()) {
            PlayerRoomInfo playerRoomInfo = entry.getValue();
            BLOKUSRoomPlayerInfo.Builder itemBuilder = BLOKUSRoomPlayerInfo.newBuilder();
            itemBuilder.setAccount(playerRoomInfo.getAccount());
            itemBuilder.setColor(playerRoomInfo.getColor().getValue());
            builder.addItmes(itemBuilder);
        }
        message.setData(builder.build().toByteArray());
        return message;
    }

    public static MessageBean formatGiveUpMessage(BlokusColor color) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.GIVE_UP);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSColor.Builder builder = BLOKUSColor.newBuilder();
        builder.setColor(color.getValue());

        message.setData(builder.build().toByteArray());
        return message;
    }

    public static MessageBean formatJoinRoomMessage(String roomName, GameType gameType) {

        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.JOIN_ROOM);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSCreateRoom.Builder builder = BLOKUSCreateRoom.newBuilder();
        builder.setRoomName(roomName);
        builder.setGameType(gameType.getValue());

        message.setData(builder.build().toByteArray());
        return message;

    }

    public static MessageBean formatRoomListMessage(Collection<RoomInfo> roomList) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.ROOM_LIST);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSRoomList.Builder builder = BLOKUSRoomList.newBuilder();
        for (RoomInfo roomInfo : roomList) {
            BLOKUSRoomInfo.Builder roomInfoBuilder = BLOKUSRoomInfo.newBuilder();
            roomInfoBuilder.setRoomName(roomInfo.getRoomName());
            roomInfoBuilder.setGameType(roomInfo.getGameType().getValue());
            roomInfoBuilder.setRoomStatus(roomInfo.getRoomStatus().getValue());
            roomInfoBuilder.setCurrentPlayers(roomInfo.getPlayerCount());
            builder.addRoomItems(roomInfoBuilder.build());
        }

        message.setData(builder.build().toByteArray());
        return message;
    }

    public static MessageBean formatRankInfoMessage(List<PlayerGameRecord> twoPlayersRanks, List<PlayerGameRecord> fourPlayersRanks) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.RANK_INFO);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSRankInfo.Builder builder = BLOKUSRankInfo.newBuilder();
        for (PlayerGameRecord record : twoPlayersRanks) {
            BLOKUSRankItem.Builder rankItem = BLOKUSRankItem.newBuilder();
            rankItem.setAccount(record.getAccount());
            rankItem.setWinCount(record.getWinCount());
            rankItem.setLoseCount(record.getLoseCount());
            rankItem.setEscapeCount(record.getEscapeCount());
            rankItem.setRankScore(record.getRankScore());
            rankItem.setRank("gold");
            builder.addTwoPlayersRankItems(rankItem.build());
        }
        for (PlayerGameRecord record : fourPlayersRanks) {
            BLOKUSRankItem.Builder rankItem = BLOKUSRankItem.newBuilder();
            rankItem.setAccount(record.getAccount());
            rankItem.setWinCount(record.getWinCount());
            rankItem.setLoseCount(record.getLoseCount());
            rankItem.setEscapeCount(record.getEscapeCount());
            rankItem.setRankScore(record.getRankScore());
            rankItem.setRank("gold");
            builder.addFourPlayersRankItems(rankItem.build());
        }

        message.setData(builder.build().toByteArray());
        return message;

    }

    public static MessageBean formatProfileMessage(List<PlayerGameRecord> playerGameRecords, List<PlayerGameLogViewBean> playerGameLogViewBeans) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.PROFILE);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSProfile.Builder builder = BLOKUSProfile.newBuilder();
        for (PlayerGameRecord record : playerGameRecords) {
            BLOKUSRankItem.Builder rankItem = BLOKUSRankItem.newBuilder();
            rankItem.setAccount(record.getAccount());
            rankItem.setWinCount(record.getWinCount());
            rankItem.setLoseCount(record.getLoseCount());
            rankItem.setEscapeCount(record.getEscapeCount());
            rankItem.setRankScore(record.getRankScore());
            rankItem.setRank("gold");
            if (GameType.blokus_four.getValue().equals(record.getGameType())) {
                builder.setFourPlayersRankItem(rankItem.build());
            } else {
                builder.setTwoPlayersRankItem(rankItem.build());
            }
        }

        for (PlayerGameLogViewBean viewBean : playerGameLogViewBeans) {
            BLOKUSPlayerGameLogItem.Builder logItem = BLOKUSPlayerGameLogItem.newBuilder();
            logItem.setResult(viewBean.getGameResult().toString());
            logItem.setGameType(viewBean.getGameType().getValue());
            logItem.setStepsCount(viewBean.getStepsCount());
            logItem.setDetail(viewBean.getDetail());
            logItem.setTime(viewBean.getTime().toString());
            builder.addPlayerGameLogs(logItem.build());
        }

        message.setData(builder.build().toByteArray());
        return message;
    }

//    public static MessageBean formatProfileMessage(List<PlayerGameRecord> records) {
//        MessageBean message = new MessageBean();
//        message.setOperationCode(OperationCode.PROFILE);
//        message.setStatusCode(StatusCode.SUCCESS);
//
//        BLOKUSRankInfo.Builder builder = BLOKUSRankInfo.newBuilder();
//        for (GameRecord record : records) {
//            BLOKUSRankItem.Builder rankItem = BLOKUSRankItem.newBuilder();
//            rankItem.setAccount(record.getAccount());
//            rankItem.setWinCount(record.getWinCount());
//            rankItem.setLoseCount(record.getLoseCount());
//            rankItem.setEscapeCount(record.getEscapeCount());
//            rankItem.setRankScore(record.getRankScore());
//            rankItem.setRank("gold");
//            if (GameType.blokus_four.getValue().equals(record.getGameType())) {
//                builder.addFourPlayersRankItems(rankItem.build());
//            } else {
//                builder.addTwoPlayersRankItems(rankItem.build());
//            }
//        }
//
//        message.setData(builder.build().toByteArray());
//        return message;
//
//    }
}
