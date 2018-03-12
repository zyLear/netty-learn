package com.zylear.netty.learn.manager;

import com.zylear.netty.learn.bean.RoomInfo;

/**
 * Created by xiezongyu on 2018/3/10.
 */
public interface ServerCacheCallback {

    void startGame(RoomInfo roomInfo);

    void updateRoomPlayersInfo(String roomName);

}
