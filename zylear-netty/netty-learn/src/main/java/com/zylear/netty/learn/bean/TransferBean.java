package com.zylear.netty.learn.bean;

import io.netty.channel.Channel;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public class TransferBean {
    private MessageBean message;
    private Channel channel;

    public TransferBean() {
    }

    public TransferBean(MessageBean message, Channel channel) {
        this.message = message;
        this.channel = channel;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
