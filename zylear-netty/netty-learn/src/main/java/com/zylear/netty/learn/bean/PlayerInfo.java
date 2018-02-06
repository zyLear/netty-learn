package com.zylear.netty.learn.bean;


import io.netty.channel.Channel;

/**
 * Created by xiezongyu on 2018/2/2.
 */
public class PlayerInfo {

    private String account;
    private String roomName;
    private Channel channel;
    private RoomInfo roomInfo;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }
}
