package com.zylear.netty.learn.test;

import com.zylear.netty.learn.bean.PlayerInfo;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiezongyu on 2018/2/2.
 */
public class PureTest {


    @Test
    public void testTObjectIntMap() {
        TObjectIntMap<PlayerInfo> map = new TObjectIntHashMap<>();
        Map<PlayerInfo, Integer> map1 = new HashMap<>();

        PlayerInfo playerInfo = new PlayerInfo();
        PlayerInfo playerInfo1 = new PlayerInfo();
        PlayerInfo playerInfo2 = new PlayerInfo();

        playerInfo.setAccount("1");
        playerInfo.setRoomName("1");
        playerInfo1.setRoomName("1");
        playerInfo.setAccount("1");
        playerInfo2.setAccount("1");
        playerInfo2.setRoomName("1");

        map.put(playerInfo, 1);
        map.put(playerInfo1, 2);
        map.put(playerInfo2, 3);

        map1.put(playerInfo, 1);
        map1.put(playerInfo1, 2);
        map1.put(playerInfo2, 3);

        System.out.println(map.get(playerInfo));
        System.out.println(map.get(playerInfo1));
        System.out.println(map.get(playerInfo2));
        System.out.println(map1.get(playerInfo));
        System.out.println(map1.get(playerInfo1));
        System.out.println(map1.get(playerInfo2));
    }

    @Test
    public void testCollections() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        System.out.println(map.values().toString());
    }

    /*
    *   void update(@Param("account") String account,
                @Param("gameType") Integer gameType,
                @Param("winCount")  Integer winCount,
                @Param("loseCount")  Integer loseCount,
                @Param("escapeCount")  Integer escapeCount,
                @Param("rankScore")  Integer rankScore);

    List<GameRecord> findRanks(@Param("gameType") Integer gameType);

    List<GameRecord> findRanksByAccount(@Param("account") String account);





     <update id="update">
        UPDATE t_game_record
        SET
            win_count    = win_count + #{winCount,jdbcType=INTEGER},
            lose_count   = lose_count + #{loseCount,jdbcType=INTEGER},
            escape_count = escape_count + #{escapeCount,jdbcType=INTEGER},
            rank_score   = rank_score + #{rankScore,jdbcType=INTEGER}
        WHERE account = #{account,jdbcType=VARCHAR}
              AND game_type = #{gameType,jdbcType=TINYINT}
    </update>

    <select id="findRanks"  resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM t_game_record
        WHERE game_type = #{gameType,jdbcType=TINYINT}
        ORDER BY rank_score DESC
    </select>

    <select id="findRanksByAccount" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM t_game_record
        WHERE account = #{account,jdbcType=VARCHAR}
    </select>





    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    *
    * */


}
