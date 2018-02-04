package com.zylear.netty.learn.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class BaseMessageQueue<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseMessageQueue.class);

    /**
     * 消息队列
     */
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();

    /**
     * 不阻塞,即刻返回;没有
     */
    public T take() {
        return queue.poll();
    }

    /**
     * 阻塞,消息不能丢掉
     */
    public void put(T t) {
        try {
            queue.put(t);
        } catch (Exception e) {
            logger.info("put message into queue fail. ", e);
        }
    }

    public int getQueueSize() {
        return queue.size();
    }

}