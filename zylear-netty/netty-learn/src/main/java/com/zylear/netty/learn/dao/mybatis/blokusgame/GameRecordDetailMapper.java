package com.zylear.netty.learn.dao.mybatis.blokusgame;

import com.zylear.netty.learn.domain.GameRecordDetail;

public interface GameRecordDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameRecordDetail record);

    int insertSelective(GameRecordDetail record);

    GameRecordDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameRecordDetail record);

    int updateByPrimaryKey(GameRecordDetail record);



}



