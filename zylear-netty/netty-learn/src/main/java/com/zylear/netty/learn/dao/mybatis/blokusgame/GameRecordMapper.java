package com.zylear.netty.learn.dao.mybatis.blokusgame;

import com.zylear.netty.learn.domain.GameRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GameRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameRecord record);

    int insertSelective(GameRecord record);

    GameRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameRecord record);

    int updateByPrimaryKey(GameRecord record);



    void update(@Param("account") String account,
                @Param("gameType") Integer gameType,
                @Param("winCount")  Integer winCount,
                @Param("loseCount")  Integer loseCount,
                @Param("escapeCount")  Integer escapeCount,
                @Param("rankScore")  Integer rankScore);

    List<GameRecord> findRanks(@Param("gameType") Integer gameType);

    List<GameRecord> findRanksByAccount(@Param("account") String account);
}