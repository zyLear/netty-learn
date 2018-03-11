package com.zylear.netty.learn.dao.mybatis.blokusgame;

import com.zylear.netty.learn.domain.GameLog;

public interface GameLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameLog record);

    int insertSelective(GameLog record);

    GameLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameLog record);

    int updateByPrimaryKey(GameLog record);
}