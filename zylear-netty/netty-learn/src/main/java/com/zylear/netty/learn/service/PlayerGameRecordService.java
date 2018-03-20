package com.zylear.netty.learn.service;

import com.zylear.netty.learn.domain.PlayerGameRecord;

import java.util.List;

/**
 * Created by xiezongyu on 2018/3/10.
 */
public interface PlayerGameRecordService {

    void insert(PlayerGameRecord gameRecord);

    List<PlayerGameRecord> findRanks(Integer gameType);

    List<PlayerGameRecord> findRanksByAccount(String account);

    void update(String account, Integer gameType, Integer winCount, Integer loseCount, Integer escapeCount, Integer rankScore);
}
