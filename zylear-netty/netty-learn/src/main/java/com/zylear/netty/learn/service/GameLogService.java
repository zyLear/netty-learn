package com.zylear.netty.learn.service;

import com.zylear.netty.learn.domain.GameLog;

/**
 * Created by xiezongyu on 2018/3/10.
 */
public interface GameLogService {

    void insert(GameLog gameLog);

    GameLog findById(Integer gameLogId);
}
