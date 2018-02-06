package com.zylear.netty.learn.manager;

import com.zylear.netty.learn.bean.TransferBean;

import java.util.List;

/**
 * Created by xiezongyu on 2018/2/2.
 */
public interface MessageHandler<T, K> {

    public void handle(T t, K k);

    public void send(K t);
}
