package com.zylear.netty.learn.cache;

import com.zylear.netty.learn.bean.PlayerInfo;
import com.zylear.netty.learn.bean.PlayerRoomInfo;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.enums.ChooseColor;
import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;
import io.netty.channel.Channel;
import io.netty.util.internal.ConcurrentSet;

import java.util.*;
import java.util.Map.Entry;
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
//    private static final Set<String> playerAccountSet = new ConcurrentSet<>();


//    public static void addPlayer(String account, Channel channel) {
//        PlayerInfo player = new PlayerInfo();
//        player.setAccount(account);
//        player.setChannel(channel);
//        playerMap.put(channel, player);
//    }

    //preliminary lock  , improve later
    public synchronized static boolean createRoom(Channel channel, String roomName, RoomType roomType) {

        if (!roomMap.containsKey(roomName)) {
            PlayerInfo playerInfo = playerMap.get(channel);
            if (playerInfo != null) {
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.setPlayerCount(1);
                roomInfo.setMaxPlayerCount(4);
                roomInfo.setRoomName(roomName);
                roomInfo.setRoomStatus(RoomStatus.waiting);

                PlayerRoomInfo playerRoomInfo = new PlayerRoomInfo();
                playerRoomInfo.setAccount(playerInfo.getAccount());
                playerRoomInfo.setReady(false);
                playerRoomInfo.setColor(ChooseColor.blue);
                playerRoomInfo.setChannel(channel);
                roomInfo.getPlayers().put(playerInfo.getAccount(), playerRoomInfo);

                playerInfo.setRoomName(roomName);
                playerInfo.setRoomInfo(roomInfo);
                roomMap.put(roomName, roomInfo);

                return true;
            }
        }
        return false;
    }

    public static boolean login(Channel channel, String account) {

        if (!isContainsAccount(account)) {
            PlayerInfo player = new PlayerInfo();
            player.setAccount(account);
            player.setChannel(channel);
//            playerAccountSet.add(account);
            playerMap.put(channel, player);
            return true;
        }
        return false;
    }

    public static void quit(Channel channel) {
        PlayerInfo playerInfo = playerMap.get(channel);
        if (playerInfo != null) {
//            playerAccountSet.remove(playerInfo.getAccount());
            RoomInfo roomInfo = playerInfo.getRoomInfo();
            if (roomInfo != null) {
                int playerCount = roomInfo.getPlayerCount();
                if (playerCount <= 1) {
                    roomMap.remove(roomInfo.getRoomName());
                } else {
                    roomInfo.setPlayerCount(playerCount - 1);
                    roomInfo.getPlayers().remove(playerInfo.getAccount());
                }
            }
        }
    }

    public static PlayerInfo getPlayerInfo(Channel channel) {
        return playerMap.get(channel);
    }

    public static boolean joinRoom(Channel channel, String roomName) {

        PlayerInfo player = playerMap.get(channel);
        if (player != null) {
            RoomInfo roomInfo = roomMap.get(roomName);
            if (player.getRoomInfo() == null && roomInfo != null &&
                    RoomStatus.waiting.equals(roomInfo.getRoomStatus()) &&
                    roomInfo.getPlayerCount() < roomInfo.getMaxPlayerCount()) {

                roomInfo.setPlayerCount(roomInfo.getPlayerCount() + 1);
                PlayerRoomInfo playerRoomInfo = new PlayerRoomInfo();
                playerRoomInfo.setAccount(player.getAccount());
                playerRoomInfo.setReady(false);
                playerRoomInfo.setColor(ChooseColor.blue);
                playerRoomInfo.setChannel(channel);

                roomInfo.getPlayers().put(player.getAccount(), playerRoomInfo);
                player.setRoomInfo(roomInfo);
                return true;
            }
        }
        return false;
    }

    public static String leaveRoom(Channel channel) {
        PlayerInfo player = playerMap.get(channel);
        if (player != null) {
            RoomInfo roomInfo = player.getRoomInfo();
            if (roomInfo != null && RoomStatus.waiting.equals(roomInfo.getRoomStatus()) &&
                    roomInfo.getPlayerCount() > 0) {
                player.setRoomInfo(null);
                if (roomInfo.getPlayerCount() == 1) {
                    roomMap.remove(roomInfo.getRoomName());
                } else {
                    roomInfo.setPlayerCount(roomInfo.getPlayerCount() - 1);
                    roomInfo.getPlayers().remove(player.getAccount());
                }
                return roomInfo.getRoomName();
            }
        }
        return null;
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


    public static Map<String, PlayerRoomInfo> getPlayerRoomInfos(String roomName) {
        try {

            return roomMap.get(roomName).getPlayers();
        } catch (Exception e) {
            return Collections.EMPTY_MAP;
        }

    }


//    public static List<Channel> getPlayersChannelInRoom(String roomName) {
//        try {
//            RoomInfo roomInfo = roomMap.get(roomName);
//            Set<String> accounts = roomInfo.getPlayers().keySet();
//            List<Channel> list = new ArrayList<>(roomInfo.getPlayerCount());
//            for (Entry<Channel, PlayerInfo> entry : playerMap.entrySet()) {
//                if (accounts.contains(entry.getValue().getAccount())) {
//                    list.add(entry.getValue().getChannel());
//                }
//            }
//            return list;
//        } catch (Exception e) {
//            return Collections.EMPTY_LIST;
//        }
//    }


    private static boolean isContainsAccount(String acocunt) {
        for (Entry<Channel, PlayerInfo> entry : playerMap.entrySet()) {
            if (acocunt.equals(entry.getValue().getAccount())) {
                return true;
            }
        }
        return false;
    }

    public static void showAllRooms() {
        for (Entry<String, RoomInfo> entry : roomMap.entrySet()) {
            entry.getValue().toString();
        }
    }


}
