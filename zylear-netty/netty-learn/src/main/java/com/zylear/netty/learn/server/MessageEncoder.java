package com.zylear.netty.learn.server;

import com.zylear.netty.learn.bean.MessageBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class MessageEncoder extends MessageToByteEncoder<MessageBean> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageBean msg, ByteBuf byteBuf) throws Exception {
        int dataLength = msg.getData() == null ? 0 : msg.getData().length;
        byteBuf.ensureWritable(8 + dataLength);
        byteBuf.writeInt(dataLength);
        byteBuf.writeShort(msg.getOperationCode());
        byteBuf.writeShort(msg.getStatusCode());
        if (dataLength > 0) {
            byteBuf.writeBytes(msg.getData());
        }
    }
}
