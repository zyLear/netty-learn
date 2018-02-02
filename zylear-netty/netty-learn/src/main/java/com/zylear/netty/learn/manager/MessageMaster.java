package com.zylear.netty.learn.manager;

import com.zylear.netty.learn.bean.MessageBean;
import com.zylear.netty.learn.queue.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiezongyu on 2018/2/2.
 */
@Component
public class MessageMaster {

    private static final Logger logger = LoggerFactory.getLogger(MessageMaster.class);

    private final int maxThreadCount = 100;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ExecutorService executorServices = Executors.newFixedThreadPool(maxThreadCount);

    private MessageManager messageManager;

    @PostConstruct
    public void start() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                getMessageLoop();
            }
        });
    }

    public void getMessageLoop() {
        while (true) {
            if (AtomicVar.currentRunningCount.get() < maxThreadCount) {
                MessageBean message = MessageQueue.getInstance().take();
                if (message != null) {
                    int count = AtomicVar.currentRunningCount.incrementAndGet();
                    logger.info("begin handle msg. current:{}", count);
                    executorServices.submit(new MessageWorker(messageManager, message));
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    @Autowired
    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }
}
