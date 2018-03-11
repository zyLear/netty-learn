package com.zylear.netty.learn.service.impl;

import com.zylear.netty.learn.dao.mybatis.blokusgame.PlayerGameLogMapper;
import com.zylear.netty.learn.domain.PlayerGameLog;
import com.zylear.netty.learn.service.PlayerGameLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xiezongyu on 2018/3/10.
 */
@Component
public class PlayerGameLogServiceImpl implements PlayerGameLogService {

    private PlayerGameLogMapper playerGameLogMapper;


    @Override
    public void insert(PlayerGameLog playerGameLog) {
        playerGameLogMapper.insert(playerGameLog);
    }

    @Override
    public List<PlayerGameLog> findByAccount(String account) {
        return playerGameLogMapper.findByAccount(account);
    }

    @Override
    public List<PlayerGameLog> findByGameLogId(Integer gameLogId) {
        return playerGameLogMapper.findByGameLogId(gameLogId);
    }


    @Autowired
    public void setPlayerGameLogMapper(PlayerGameLogMapper playerGameLogMapper) {
        this.playerGameLogMapper = playerGameLogMapper;
    }
}
