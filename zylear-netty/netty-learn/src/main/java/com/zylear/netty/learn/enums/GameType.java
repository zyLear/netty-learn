package com.zylear.netty.learn.enums;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public enum GameType {
    unknown(-1),
    blokus_four(1),
    blokus_two(2);


    private Integer value;

    GameType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static GameType valueOf(Integer value) {
        try {
            for (GameType gameType : GameType.values()) {
                if (gameType.getValue().equals(value)) {
                    return gameType;
                }
            }
        } catch (Exception e) {
        }
        return unknown;
    }
}
