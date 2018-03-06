package com.zylear.netty.learn.enums;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public enum GameStatus {

    win(1),
    fail(2),
    gaming(3)
    ;

    private Integer value;

    GameStatus(Integer value) {
        this.value=value;
    }

}
