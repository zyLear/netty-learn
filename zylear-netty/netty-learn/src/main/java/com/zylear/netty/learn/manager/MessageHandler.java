package com.zylear.netty.learn.manager;

/**
 * Created by xiezongyu on 2018/2/2.
 */
public interface MessageHandler<T, K> {

    public K handle(T t);

    public void send(K t);
}
