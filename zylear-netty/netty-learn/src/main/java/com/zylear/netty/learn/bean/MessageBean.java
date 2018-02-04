package com.zylear.netty.learn.bean;

import com.zylear.netty.learn.constant.OperationCode;
import com.zylear.netty.learn.constant.StatusCode;
import io.netty.channel.Channel;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class MessageBean {

    private short operationCode;
    private short statusCode;
    private byte[] data;



    public static final MessageBean LOGIN_SUCCESS = new MessageBean(OperationCode.LOGIN, StatusCode.SUCCESS);
    public static final MessageBean LOGIN_FAIL = new MessageBean(OperationCode.LOGIN, StatusCode.FAIL);
    public static final MessageBean CREATE_ROOM_SUCCESS = new MessageBean(OperationCode.CREATE_ROOM, StatusCode.SUCCESS);
    public static final MessageBean CREATE_ROOM_FAIL = new MessageBean(OperationCode.CREATE_ROOM, StatusCode.FAIL);

    public MessageBean() {

    }

//    public static MessageBean loginSucess(Channel channel) {
//        return new MessageBean(OperationCode.LOGIN, StatusCode.SUCCESS, channel);
//    }
//
//    public static MessageBean loginFail(Channel channel) {
//        return new MessageBean(OperationCode.LOGIN, StatusCode.FAIL, channel);
//    }
//
//    public static MessageBean createRoomSucess(Channel channel) {
//        return new MessageBean(OperationCode.CREATE_ROOM, StatusCode.SUCCESS);
//    }
//
//    public static MessageBean createRoomFail(Channel channel) {
//        return new MessageBean(OperationCode.CREATE_ROOM, StatusCode.FAIL);
//    }
//
    public MessageBean(short operationCode, short statusCode) {
        this.operationCode = operationCode;
        this.statusCode = statusCode;
    }

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

    @Override
    public String toString() {
        return "MessageBean{" +
                "operationCode=" + operationCode +
                ", statusCode=" + statusCode +
                '}';
    }
}
