package com.zylear.netty.learn.service.impl;

import com.zylear.netty.learn.dao.mybatis.blokusgame.GameRecordDetailMapper;
import com.zylear.netty.learn.domain.GameRecordDetail;
import com.zylear.netty.learn.service.GameRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiezongyu on 2018/3/8.
 */
@Component
public class GameRecordDetailServiceImpl implements GameRecordDetailService{

    private GameRecordDetailMapper gameRecordDetailMapper;


    @Override
    public void insert(GameRecordDetail gameRecordDetail) {
        gameRecordDetailMapper.insert(gameRecordDetail);
    }


    @Autowired
    public void setGameRecordDetailMapper(GameRecordDetailMapper gameRecordDetailMapper) {
        this.gameRecordDetailMapper = gameRecordDetailMapper;
    }


}
