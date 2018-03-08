package com.zylear.netty.learn.enums;

/**
 * Created by xiezongyu on 2018/3/8.
 */
public enum GameResult {
    unknown(-1),
    win(1),
    lose(2),
    escape(3);


    private Integer value;

    GameResult(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static GameResult valueOf(Integer value) {
        try {
            for (GameResult gameResult : GameResult.values()) {
                if (gameResult.getValue().equals(value)) {
                    return gameResult;
                }
            }
        } catch (Exception e) {
        }
        return unknown;
    }
}
