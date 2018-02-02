package com.zylear.netty.learn.cache;

import com.zylear.netty.learn.bean.PlayerInfo;
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
    public static final Map<String, String> playerInfo = new ConcurrentHashMap<>();
    public static final Map<Channel, PlayerInfo> players = new ConcurrentHashMap<>();

    public static void addPlayer(String account, Channel channel) {
        PlayerInfo player = new PlayerInfo();
        player.setAccount(account);
        player.setChannel(channel);
        players.put(channel, player);
    }

    public static boolean createRoom(String account, String roomName) {


        return false;
    }


}
