package com.zylear.netty.learn.manager;


import com.google.gson.Gson;
import com.zylear.netty.contract.BaseOuterClass.BaseMessage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Component
public class BlokusMessageManager {

//    public static final ChannelGroup loginChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static final ChannelGroup connectChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static final Map<String, Channel> loginChannel = new ConcurrentHashMap<>();
    public static final Map<String, ChannelGroup> roomChannelGroupMap = new ConcurrentHashMap<>();


    public void handle(Channel channel, String msg) {

        System.out.println(msg);
//        BaseMessage baseMessage=BaseMessage.parsefrom


//        channel.writeAndFlush("im server\n");
//        Gson gson = new Gson();

    }

    public void handle(Channel channel, byte[] bytes) {

//        System.out.println(msg);
//        BaseMessage baseMessage=BaseMessage.parsefrom




//        channel.writeAndFlush("im server\n");
//        Gson gson = new Gson();

    }

//    Gson gson = new Gson();
//
//    JBean2 bean = gson.fromJson(JBean.getjDate(), JBean2.class);
//
//
//（bean to json）
//            [java] view plain copy
//    JBean2 bean = new JBean2();
//
//bean.initBean();
//
//System.out.println(gson.toJson(bean));




}
