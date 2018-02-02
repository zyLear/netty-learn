package com.zylear.netty.learn.manager;


import com.google.protobuf.InvalidProtocolBufferException;
import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.proto.AccountOuterClass.NETTYAccount;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 28444
 * @date 2018/1/10.
 */
@Component
public class MessageManager implements MessageHandler<MessageBean, List<MessageBean>> {

    public List<MessageBean> handle(MessageBean message) {
        System.out.println(message.toString());

        if (message.getOperationCode() == 0) {
            NETTYAccount nettyAccount = null;
            try {
                nettyAccount = NETTYAccount.parseFrom(message.getData());
                System.out.println(nettyAccount.getAccount());
                System.out.println(nettyAccount.getPassword());
            } catch (InvalidProtocolBufferException e) {
            }
        }

        message.setOperationCode((short) 5);
        message.setStatusCode((short) 0);
        return Arrays.asList(message);
    }


    public void send(List<MessageBean> messages) {
        if (messages != null) {
            for (MessageBean message : messages) {
                message.getChannel().writeAndFlush(message);
            }
        }
    }


}
