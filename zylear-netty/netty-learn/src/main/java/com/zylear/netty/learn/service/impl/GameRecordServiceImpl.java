package com.zylear.netty.learn.service.impl;

import com.zylear.netty.learn.dao.mybatis.blokusgame.GameRecordMapper;
import com.zylear.netty.learn.domain.GameRecord;
import com.zylear.netty.learn.service.GameRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiezongyu on 2018/3/7.
 */
@Component
public class GameRecordServiceImpl implements GameRecordService {

    private GameRecordMapper gameRecordMapper;


    @Override
    public void insert(GameRecord gameRecord) {
        gameRecordMapper.insert(gameRecord);
    }

    @Autowired
    public void setGameRecordMapper(GameRecordMapper gameRecordMapper) {
        this.gameRecordMapper = gameRecordMapper;
    }


}