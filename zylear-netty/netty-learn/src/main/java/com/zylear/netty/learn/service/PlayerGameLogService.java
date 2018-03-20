package com.zylear.netty.learn.service;

import com.zylear.netty.learn.domain.PlayerGameLog;

import java.util.List;

/**
 * Created by xiezongyu on 2018/3/10.
 */
public interface PlayerGameLogService {

    void insert(PlayerGameLog playerGameLog);

    List<PlayerGameLog> findByAccount(String account);

    List<PlayerGameLog> findByGameLogId(Integer gameLogId);
}
