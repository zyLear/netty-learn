package com.zylear.netty.learn.client;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import com.zylear.proto.BlokusOuterClass.BLOKUSAccount;
import com.zylear.proto.BlokusOuterClass.BLOKUSCreateRoom;
import com.zylear.proto.BlokusOuterClass.BLOKUSRoomName;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * @author 28444
 * @date 2018/1/9.
 */
public class SimpleChatClient {

    private static String host = "localhost";
    private static int port = 9090;

    public static void main(String[] args) throws Exception {
        new SimpleChatClient(host, port).run();
    }

    public SimpleChatClient(String host, int port) {
        SimpleChatClient.host = host;
        SimpleChatClient.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChatClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            Scanner sc = new Scanner(System.in);
            System.out.println("please enter...");
            boolean exit = false;
            // 输入exit，退出系统
            while (!exit) {
                String str = sc.next();
//                channel.writeAndFlush(str + "\r\n");

//                sendLogin(channel);

                if ("0".equals(str)) {
                    sendLoginTwo(channel);
                } else if ("1".equals(str)) {
                    sendLogin(channel);
                } else if ("2".equals(str)) {
                    sendCreateRoom(channel);
                } else if ("3".equals(str)) {
                    sendLeaveRoom(channel);
                } else if ("4".equals(str)) {
                    sendJoinRoom(channel);
                }

                if (str.equalsIgnoreCase("quit")) {
                    exit = true;
                    channel.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private void sendLeaveRoom(Channel channel) {

        MessageBean messageBean = new MessageBean();
        messageBean.setOperationCode(OperationCode.LEAVE_ROOM);
        messageBean.setStatusCode(StatusCode.SUCCESS);
//        BLOKUSCreateRoom.Builder builder = BLOKUSCreateRoom.newBuilder();
//        builder.setRoomName("123456");
//        builder.setGameType(2);
//        messageBean.setData(builder.build().toByteArray());
        channel.writeAndFlush(messageBean);
        System.out.println("sendLeaveRoom success");

    }

    private void sendLoginTwo(Channel channel) {
        MessageBean messageBean = new MessageBean();
        messageBean.setOperationCode(OperationCode.LOGIN);
        messageBean.setStatusCode(StatusCode.SUCCESS);
        BLOKUSAccount.Builder builder = BLOKUSAccount.newBuilder();
        builder.setAccount("654321");
        builder.setPassword("123456");
        messageBean.setData(builder.build().toByteArray());
        channel.writeAndFlush(messageBean);
        System.out.println("sendLogin success");
    }

    private void sendLogin(Channel channel) {

        MessageBean messageBean = new MessageBean();
        messageBean.setOperationCode(OperationCode.LOGIN);
        messageBean.setStatusCode(StatusCode.SUCCESS);
        BLOKUSAccount.Builder builder = BLOKUSAccount.newBuilder();
        builder.setAccount("123456");
        builder.setPassword("123456");
        messageBean.setData(builder.build().toByteArray());
        channel.writeAndFlush(messageBean);
        System.out.println("sendLogin success");
    }

    private void sendCreateRoom(Channel channel) {

        MessageBean messageBean = new MessageBean();
        messageBean.setOperationCode(OperationCode.CREATE_ROOM);
        messageBean.setStatusCode(StatusCode.SUCCESS);
        BLOKUSCreateRoom.Builder builder = BLOKUSCreateRoom.newBuilder();
        builder.setRoomName("roomName");
        builder.setGameType(2);
        messageBean.setData(builder.build().toByteArray());
        channel.writeAndFlush(messageBean);
        System.out.println("sendCreateRoom success");

    }

    private void sendJoinRoom(Channel channel) {

        MessageBean messageBean = new MessageBean();
        messageBean.setOperationCode(OperationCode.JOIN_ROOM);
        messageBean.setStatusCode(StatusCode.SUCCESS);
        BLOKUSRoomName.Builder builder = BLOKUSRoomName.newBuilder();
        builder.setRoomName("roomName");
        messageBean.setData(builder.build().toByteArray());
        channel.writeAndFlush(messageBean);
        System.out.println("sendJoinRoom success");

    }
}