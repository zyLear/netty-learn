package com.zylear.netty.learn.enums;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public enum RoomStatus {

    waiting(1),
    gaming(2)
    ;

    private Integer value;

    RoomStatus(Integer value) {
        this.value=value;
    }

}
