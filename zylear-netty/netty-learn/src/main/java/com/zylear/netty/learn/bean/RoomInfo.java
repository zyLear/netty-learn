package com.zylear.netty.learn.bean;

import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;

import java.util.HashSet;
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
    private Set<String> players = new HashSet<>();

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

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }
}
