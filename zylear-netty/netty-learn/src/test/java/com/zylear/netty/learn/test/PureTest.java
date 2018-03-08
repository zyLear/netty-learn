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


}
