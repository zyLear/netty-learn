package com.zylear.netty.learn.service;

import com.zylear.netty.learn.domain.GameRecord;

import java.util.List;

/**
 * Created by xiezongyu on 2018/3/7.
 */
public interface GameRecordService {
    void insert(GameRecord gameRecord) ;

    void update(String account, Integer gameType, Integer winCount, Integer loseCount, Integer escapeCount, Integer rankScore);

    List<GameRecord> findRanks(Integer gameType);

    List<GameRecord> findRanksByAccount(String account);
}
