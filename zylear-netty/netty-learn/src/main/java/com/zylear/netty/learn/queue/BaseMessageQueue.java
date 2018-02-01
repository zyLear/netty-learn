package com.zylear.netty.learn.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 28444
 * @date 2018/2/1.
 */
public class BaseMessageQueue <T> {

    /** 消息队列 */
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();

    /**
     * 不阻塞,即刻返回;没有
     * */
    public T take() {
        return queue.poll();
    }

    /**
     * 阻塞,消息不能丢掉
     * */
    public void put(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getQueueSize() {
        return queue.size();
    }

}