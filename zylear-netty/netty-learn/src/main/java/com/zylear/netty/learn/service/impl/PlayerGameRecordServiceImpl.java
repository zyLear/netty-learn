package com.zylear.netty.learn.service.impl;

import com.zylear.netty.learn.dao.mybatis.blokusgame.PlayerGameRecordMapper;
import com.zylear.netty.learn.domain.PlayerGameRecord;
import com.zylear.netty.learn.service.PlayerGameRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xiezongyu on 2018/3/10.
 */
@Component
public class PlayerGameRecordServiceImpl implements PlayerGameRecordService {

    private PlayerGameRecordMapper playerGameRecordMapper;

    @Override
    public void insert(PlayerGameRecord gameRecord) {
        playerGameRecordMapper.insert(gameRecord);
    }

    @Override
    public List<PlayerGameRecord> findRanks(Integer gameType) {
        return playerGameRecordMapper.findRanks(gameType);
    }

    @Override
    public List<PlayerGameRecord> findRanksByAccount(String account) {
        return playerGameRecordMapper.findRanksByAccount(account);
    }

    @Override
    public void update(String account, Integer gameType, Integer winCount, Integer loseCount, Integer escapeCount, Integer rankScore) {
        playerGameRecordMapper.update(account, gameType, winCount, loseCount, escapeCount, rankScore);

    }

    @Autowired
    public void setPlayerGameRecordMapper(PlayerGameRecordMapper playerGameRecordMapper) {
        this.playerGameRecordMapper = playerGameRecordMapper;
    }
}
