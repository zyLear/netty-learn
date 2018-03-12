package com.zylear.netty.learn.bean;

import com.zylear.netty.learn.enums.BlokusColor;
import com.zylear.netty.learn.enums.GameStatus;
import com.zylear.netty.learn.enums.GameType;
import com.zylear.netty.learn.enums.RoomStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public class RoomInfo {

    private Integer gameLogId;
    private String roomName;
    private RoomStatus roomStatus;
    private GameType gameType;
    private Integer playerCount;
    private Integer maxPlayerCount;
    private Map<String, PlayerRoomInfo> players = new HashMap<>(4);

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(Integer playerCount) {
        this.playerCount = playerCount;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Integer getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public void setMaxPlayerCount(Integer maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public Map<String, PlayerRoomInfo> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, PlayerRoomInfo> players) {
        this.players = players;
    }

    public Integer getGameLogId() {
        return gameLogId;
    }

    public void setGameLogId(Integer gameLogId) {
        this.gameLogId = gameLogId;
    }

    public Boolean canStartGame() {
        int count = 0;
        for (Entry<String, PlayerRoomInfo> entry : players.entrySet()) {
            if (entry.getValue().getReady()) {
                count++;
            }
        }
        if (count == maxPlayerCount - 1) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String toString() {
        return "RoomInfo{" +
                "roomName='" + roomName + '\'' +
                ", roomStatus=" + roomStatus +
                ", gameType=" + gameType +
                ", playerCount=" + playerCount +
                ", maxPlayerCount=" + maxPlayerCount +
                ", playerRoomInfo=" + showPlayerRoomInfo() +
                '}';
    }

    private String showPlayerRoomInfo() {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (Entry<String, PlayerRoomInfo> entry : players.entrySet()) {
            stringBuilder.append(entry.getValue().toString());
        }
        return stringBuilder.append("]").toString();
    }

    public boolean canReady() {
        if (players.size() == maxPlayerCount) {
            Set<BlokusColor> colorSet = new HashSet<>();
            for (Entry<String, PlayerRoomInfo> entry : players.entrySet()) {
                colorSet.add(entry.getValue().getColor());
            }
            if (colorSet.size() == maxPlayerCount) {
                return true;
            }
        }
        return false;
    }

    public void checkRoomStatus() {
        for (Entry<String, PlayerRoomInfo> entry : players.entrySet()) {
            if (GameStatus.gaming.equals(entry.getValue().getGameStatus())) {
                return;
            }
        }
        roomStatus = RoomStatus.waiting;
    }
}
