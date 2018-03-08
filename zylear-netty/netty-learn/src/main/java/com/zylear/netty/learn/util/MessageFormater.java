package com.zylear.netty.learn.util;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.PlayerRoomInfo;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import com.zylear.netty.learn.domain.GameRecord;
import com.zylear.netty.learn.enums.ChooseColor;
import com.zylear.netty.learn.enums.RoomType;
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

    public static MessageBean formatGiveUpMessage(ChooseColor color) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.GIVE_UP);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSChooseColor.Builder builder = BLOKUSChooseColor.newBuilder();
        builder.setColor(color.getValue());

        message.setData(builder.build().toByteArray());
        return message;
    }

    public static MessageBean formatJoinRoomMessage(String roomName, RoomType roomType) {

        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.JOIN_ROOM);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSCreateRoom.Builder builder = BLOKUSCreateRoom.newBuilder();
        builder.setRoomName(roomName);
        builder.setRoomType(roomType.getValue());

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
            roomInfoBuilder.setRoomType(roomInfo.getRoomType().getValue());
            roomInfoBuilder.setRoomStatus(roomInfo.getRoomStatus().getValue());
            roomInfoBuilder.setCurrentPlayers(roomInfo.getPlayerCount());
            builder.addRoomItems(roomInfoBuilder.build());
        }

        message.setData(builder.build().toByteArray());
        return message;
    }

    public static MessageBean formatRankInfoMessage(List<GameRecord> twoPlayersRanks, List<GameRecord> fourPlayersRanks) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.RANK_INFO);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSRankInfo.Builder builder = BLOKUSRankInfo.newBuilder();
        for (GameRecord record : twoPlayersRanks) {
            BLOKUSRankItem.Builder rankItem = BLOKUSRankItem.newBuilder();
            rankItem.setAccount(record.getAccount());
            rankItem.setWinCount(record.getWinCount());
            rankItem.setLoseCount(record.getLoseCount());
            rankItem.setEscapeCount(record.getEscapeCount());
            rankItem.setRankScore(record.getRankScore());
            rankItem.setRank("gold");
            builder.addTwoPlayersRankItems(rankItem.build());
        }
        for (GameRecord record : fourPlayersRanks) {
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

    public static MessageBean formatProfileMessage(List<GameRecord> records) {
        MessageBean message = new MessageBean();
        message.setOperationCode(OperationCode.PROFILE);
        message.setStatusCode(StatusCode.SUCCESS);

        BLOKUSRankInfo.Builder builder = BLOKUSRankInfo.newBuilder();
        for (GameRecord record : records) {
            BLOKUSRankItem.Builder rankItem = BLOKUSRankItem.newBuilder();
            rankItem.setAccount(record.getAccount());
            rankItem.setWinCount(record.getWinCount());
            rankItem.setLoseCount(record.getLoseCount());
            rankItem.setEscapeCount(record.getEscapeCount());
            rankItem.setRankScore(record.getRankScore());
            rankItem.setRank("gold");
            if (RoomType.blokus_four.getValue().equals(record.getGameType())) {
                builder.addFourPlayersRankItems(rankItem.build());
            } else {
                builder.addTwoPlayersRankItems(rankItem.build());
            }
        }

        message.setData(builder.build().toByteArray());
        return message;

    }
}
