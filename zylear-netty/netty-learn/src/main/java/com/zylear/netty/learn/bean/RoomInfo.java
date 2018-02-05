package com.zylear.netty.learn.bean;

import com.zylear.netty.learn.enums.ChooseColor;
import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;

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

    private String roomName;
    private RoomStatus roomStatus;
    private RoomType roomType;
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

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
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

    public Boolean canStartGame() {

        for (Entry<String, PlayerRoomInfo> entry : players.entrySet()) {
            if (!entry.getValue().getReady()) {
                return false;
            }
        }
        return true;
    }
}
