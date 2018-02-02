package com.zylear.netty.learn.bean;

import io.netty.channel.Channel;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class MessageBean {

    private short operationCode;
    private short statusCode;
    private byte[] data;
    private Channel channel;

    public short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(short statusCode) {
        this.statusCode = statusCode;
    }

    public short getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(short operationCode) {
        this.operationCode = operationCode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "operationCode=" + operationCode +
                ", statusCode=" + statusCode +
                '}';
    }
}
