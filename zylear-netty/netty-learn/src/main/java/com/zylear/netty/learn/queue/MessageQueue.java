package com.zylear.netty.learn.queue;

import com.zylear.netty.learn.bean.MessageBean;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class MessageQueue extends BaseMessageQueue<MessageBean> {

    private static final MessageQueue INSTANCE = new MessageQueue();

    public static MessageQueue getInstance() {
        return INSTANCE;
    }
}
