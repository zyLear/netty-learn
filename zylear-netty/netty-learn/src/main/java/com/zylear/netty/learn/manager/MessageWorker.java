package com.zylear.netty.learn.manager;

import com.zylear.netty.learn.bean.MessageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xiezongyu on 2018/2/2.
 */
public class MessageWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);

    private MessageHandler<MessageBean, List<MessageBean>> messageHandler;
    private MessageBean message;

    public MessageWorker(MessageHandler messageHandler, MessageBean message) {
        this.messageHandler = messageHandler;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            if (messageHandler != null) {
                List<MessageBean> responses = messageHandler.handle(message);
                messageHandler.send(responses);
            }
        } catch (Exception e) {
            logger.info("handle msg exception");
        } finally {
            int count = AtomicVar.currentRunningCount.decrementAndGet();
            logger.info("handle msg end. current:{}", count);
        }

    }
}
