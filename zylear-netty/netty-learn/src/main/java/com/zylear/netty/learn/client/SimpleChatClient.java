package com.zylear.netty.learn.client;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.netty.SimpleChatClientInitializer;
import com.zylear.proto.BlokusOuterClass.BLOKUSAccount;
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

                send(channel);

                if (str.equalsIgnoreCase("exit")) {
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

    private void send(Channel channel) {

        MessageBean messageBean = new MessageBean();
        messageBean.setOperationCode((short) 0);
        messageBean.setStatusCode((short) 0);
        BLOKUSAccount.Builder builder = BLOKUSAccount.newBuilder();
        builder.setAccount("123456");
        builder.setPassword("xzyxzy");
        messageBean.setData(builder.build().toByteArray());
        channel.writeAndFlush(messageBean);
        System.out.println("send success");
    }
}