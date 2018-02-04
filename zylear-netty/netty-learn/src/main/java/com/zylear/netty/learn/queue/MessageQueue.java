package com.zylear.netty.learn.queue;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.bean.TransferBean;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class MessageQueue extends BaseMessageQueue<TransferBean> {

    private static final MessageQueue INSTANCE = new MessageQueue();

    public static MessageQueue getInstance() {
        return INSTANCE;
    }
}
