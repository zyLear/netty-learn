package com.zylear.netty.learn.enums;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public enum GameStatus {

    unknown(-1),
    win(1),
    fail(2),
    gaming(3);

    private Integer value;

    GameStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static GameStatus valueOf(Integer value) {
        try {
            for (GameStatus gameStatus : GameStatus.values()) {
                if (gameStatus.getValue().equals(value)) {
                    return gameStatus;
                }
            }
        } catch (Exception e) {
        }
        return unknown;
    }
}
