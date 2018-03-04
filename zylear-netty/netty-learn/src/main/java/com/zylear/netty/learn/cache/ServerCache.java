package com.zylear.netty.learn.cache;

import com.zylear.netty.learn.bean.PlayerInfo;
import com.zylear.netty.learn.bean.PlayerRoomInfo;
import com.zylear.netty.learn.bean.RoomInfo;
import com.zylear.netty.learn.enums.ChooseColor;
import com.zylear.netty.learn.enums.RoomStatus;
import com.zylear.netty.learn.enums.RoomType;
import io.netty.channel.Channel;
import io.netty.util.internal.ConcurrentSet;

import java.nio.channels.Channels;
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
                roomInfo.setRoomType(roomType);
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
                player.setRoomName(roomName);
                return true;
            }
        }
        return false;
    }

    public static String leaveRoom(Channel channel) {
        PlayerInfo player = playerMap.get(channel);
        if (player != null) {
            RoomInfo roomInfo = player.getRoomInfo();
            if (roomInfo != null && /*RoomStatus.waiting.equals(roomInfo.getRoomStatus()) &&*/
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

    public static void startGame(String account, String roomName) {
        RoomInfo roomInfo = roomMap.get(roomName);
        if (roomInfo != null) {

        }
    }

    public static int ready(String account, String roomName) {
        RoomInfo roomInfo = roomMap.get(roomName);
        if (roomInfo != null) {
            PlayerRoomInfo playerRoomInfo = roomInfo.getPlayers().get(account);
            if (playerRoomInfo != null) {
                boolean ready = playerRoomInfo.getReady();
                if (!ready) {

                    //**************just for test***************
                    roomInfo.setRoomStatus(RoomStatus.gaming);
                    for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                        entry.getValue().setReady(false);
                        return 0;
                    }
                    //********************************************

                    if (roomInfo.canStartGame()) {
                        roomInfo.setRoomStatus(RoomStatus.gaming);
                        for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                            entry.getValue().setReady(false);
                            return 0;
                        }
                    } else if (roomInfo.canReady()) {
                        roomInfo.getPlayers().get(account).setReady(true);
                        return 1;
                    }
                } else {
                    playerRoomInfo.setReady(false);
                    return 1;
                }
            }
        }
        return 2;
    }


    public static Map<String, PlayerRoomInfo> getPlayerRoomInfos(String roomName) {
        try {
            return roomMap.get(roomName).getPlayers();
        } catch (Exception e) {
            return Collections.EMPTY_MAP;
        }
    }

    public static void removePlayer(Channel channel) {
        playerMap.remove(channel);
    }

    public static void removeRoom(String roomName) {
        roomMap.remove(roomName);
    }

    public static RoomInfo getRoomInfo(String roomName) {
        return roomMap.get(roomName);

    }

    public static void chooseColor(String account, String roomName, ChooseColor chooseColor) {
        RoomInfo roomInfo = roomMap.get(roomName);
        if (roomInfo != null) {
            for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                if (entry.getKey().equals(account)) {
                    entry.getValue().setColor(chooseColor);
                }
                entry.getValue().setReady(false);
            }
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


    public static List<Channel> getPlayersInRoom(Channel channel) {

        PlayerInfo player = playerMap.get(channel);

        List<Channel> channels = new ArrayList<>(4);
        if (player != null) {
            RoomInfo roomInfo = player.getRoomInfo();
            if (roomInfo != null) {
                for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                    channels.add(entry.getValue().getChannel());
                }
                return channels;
            }
        }
        return Collections.EMPTY_LIST;
    }

    public static List<Channel> getOtherPlayersInRoom(Channel channel) {

        PlayerInfo player = playerMap.get(channel);

        List<Channel> channels = new ArrayList<>(4);
        if (player != null) {
            String account = player.getAccount();
            RoomInfo roomInfo = player.getRoomInfo();
            if (roomInfo != null) {
                for (Entry<String, PlayerRoomInfo> entry : roomInfo.getPlayers().entrySet()) {
                    if (!account.equals(entry.getValue().getAccount())) {
                        channels.add(entry.getValue().getChannel());
                    }
                }
                return channels;
            }
        }
        return Collections.EMPTY_LIST;
    }

    public static PlayerRoomInfo getPlayerRoomInfo(Channel channel) {
        PlayerInfo player = playerMap.get(channel);
        if (player != null) {
            RoomInfo roomInfo = player.getRoomInfo();
            if (roomInfo != null) {
                return roomInfo.getPlayers().get(player.getAccount());
            }
        }
        return null;
    }
}


