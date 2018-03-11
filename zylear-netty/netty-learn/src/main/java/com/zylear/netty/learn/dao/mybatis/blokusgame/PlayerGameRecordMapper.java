package com.zylear.netty.learn.dao.mybatis.blokusgame;

import com.zylear.netty.learn.domain.PlayerGameRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayerGameRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlayerGameRecord record);

    int insertSelective(PlayerGameRecord record);

    PlayerGameRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlayerGameRecord record);

    int updateByPrimaryKey(PlayerGameRecord record);



    void update(@Param("account") String account,
                @Param("gameType") Integer gameType,
                @Param("winCount")  Integer winCount,
                @Param("loseCount")  Integer loseCount,
                @Param("escapeCount")  Integer escapeCount,
                @Param("rankScore")  Integer rankScore);

    List<PlayerGameRecord> findRanks(@Param("gameType") Integer gameType);

    List<PlayerGameRecord> findRanksByAccount(@Param("account") String account);
}