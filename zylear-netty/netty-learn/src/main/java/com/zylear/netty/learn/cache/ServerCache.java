package com.zylear.netty.learn.cache;

import com.zylear.netty.learn.bean.PlayerInfo;
import com.zylear.netty.learn.bean.PlayerRoomInfo;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.enums.ChooseColor;
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
//    public static final Map<String, PlayerInfo> loginChannel = new ConcurrentHashMap<>();
//    public static final Map<String, ChannelGroup> roomChannelGroupMap = new ConcurrentHashMap<>();
//    public static final Map<String, String> playerInfo = new ConcurrentHashMap<>();

    private static final Map<Channel, PlayerInfo> playerMap = new ConcurrentHashMap<>();
    private static final Map<String, RoomInfo> roomMap = new ConcurrentHashMap<>();

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
            PlayerRoomInfo playerRoomInfo = new PlayerRoomInfo();
            playerRoomInfo.setAccount(playerInfo.getAccount());
            playerRoomInfo.setReady(false);
            playerRoomInfo.setColor(ChooseColor.blue);
            roomInfo.getPlayers().put(playerInfo.getAccount(), playerRoomInfo);
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

    public static PlayerInfo getPlayerInfo(Channel channel) {
        return playerMap.get(channel);
    }

    public static boolean joinRoom(Channel channel, String roomName) {

        PlayerInfo player = playerMap.get(channel);
        if (player == null) {
            return false;
        } else {
            if (player.getRoomName() != null) {
                return false;
            }

            RoomInfo roomInfo = roomMap.get(roomName);
            if (roomInfo == null) {
                return false;
            } else {
                if (RoomStatus.waiting.equals(roomInfo.getRoomStatus()) &&
                        roomInfo.getPlayerCount() < roomInfo.getMaxPlayerCount()) {
                    player.setRoomName(roomName);
                    roomInfo.setPlayerCount(roomInfo.getPlayerCount() + 1);
                    PlayerRoomInfo playerRoomInfo = new PlayerRoomInfo();
                    playerRoomInfo.setAccount(player.getAccount());
                    playerRoomInfo.setReady(false);
                    playerRoomInfo.setColor(ChooseColor.blue);
                    roomInfo.getPlayers().put(player.getAccount(), playerRoomInfo);
                    roomInfo.getPlayers().put(player.getAccount(), playerRoomInfo);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public static boolean leaveRoom(Channel channel) {
        PlayerInfo player = playerMap.get(channel);
        if (player == null) {
            return false;
        } else {
            String roomName = player.getRoomName();
            if (roomName == null) {
                return false;
            }

            RoomInfo roomInfo = roomMap.get(roomName);
            if (roomInfo == null) {
                return false;
            } else {
                if (RoomStatus.waiting.equals(roomInfo.getRoomStatus()) &&
                        roomInfo.getPlayerCount() > 0) {
                    player.setRoomName(null);
                    roomInfo.setPlayerCount(roomInfo.getPlayerCount() - 1);
                    roomInfo.getPlayers().remove(player.getAccount());
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public static void ready(Channel channel) {
        PlayerInfo player = playerMap.get(channel);
        if (player == null) {
            return;
        } else {
            String roomName = player.getRoomName();
            if (roomName == null) {
                return;
            }

            RoomInfo roomInfo = roomMap.get(roomName);
            if (roomInfo == null) {
                return;
            } else {
                if (RoomStatus.waiting.equals(roomInfo.getRoomStatus()) &&
                        roomInfo.getPlayerCount() > 0) {



                    return;
                } else {
                    return;
                }
            }
        }

    }
}
