package com.zylear.netty.learn.dao.mybatis.blokusgame;

import com.zylear.netty.learn.domain.GameRecord;

public interface GameRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameRecord record);

    int insertSelective(GameRecord record);

    GameRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameRecord record);

    int updateByPrimaryKey(GameRecord record);



    void update(String account, Integer gameType, Integer winCount, Integer loseCount, Integer escapeCount, Integer rankScore);
}