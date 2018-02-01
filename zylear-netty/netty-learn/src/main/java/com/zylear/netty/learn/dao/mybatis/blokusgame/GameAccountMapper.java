package com.zylear.netty.learn.dao.mybatis.blokusgame;

import com.zylear.netty.learn.domain.GameAccount;

public interface GameAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameAccount record);

    int insertSelective(GameAccount record);

    GameAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameAccount record);

    int updateByPrimaryKey(GameAccount record);
}