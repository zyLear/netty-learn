package com.zylear.netty.learn.cache;

import com.zylear.netty.learn.bean.PlayerInfo;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;
import com.zylear.proto.BlokusOuterClass.BLOKUSAccount;
import gnu.trove.procedure.TObjectObjectProcedure;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class ServerCache {

    //    public static final ChannelGroup connectChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static final Map<String, PlayerInfo> loginChannel = new ConcurrentHashMap<>();
    public static final Map<String, ChannelGroup> roomChannelGroupMap = new ConcurrentHashMap<>();
//    public static final Map<String, String> playerInfo = new ConcurrentHashMap<>();

    public static final Map<Channel, PlayerInfo> playerMap = new ConcurrentHashMap<>();
    public static final Map<String, RoomInfo> roomMap = new ConcurrentHashMap<>();

//    public static void addPlayer(String account, Channel channel) {
//        PlayerInfo player = new PlayerInfo();
//        player.setAccount(account);
//        player.setChannel(channel);
//        playerMap.put(channel, player);
//    }

    //preliminary lock  , improve later
    public synchronized static boolean createRoom(Channel channel, String roomName, RoomType roomType) {

        if (roomMap.containsKey(roomName)) {
            return false;
        } else {

            PlayerInfo playerInfo = playerMap.get(channel);
            if (playerInfo == null) {
                return false;
            }
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setPlayerCount(1);
            roomInfo.setMaxPlayerCount(4);
            roomInfo.setRoomName(roomName);
            roomInfo.setRoomStatus(RoomStatus.waiting);
            roomInfo.getPlayers().add(playerInfo.getAccount());
            playerInfo.setRoomName(roomName);

            return true;
        }
    }

    public static void login(Channel channel, String account) {

        PlayerInfo player = new PlayerInfo();
        player.setAccount(account);
        player.setChannel(channel);
        playerMap.put(channel, player);

    }
}
