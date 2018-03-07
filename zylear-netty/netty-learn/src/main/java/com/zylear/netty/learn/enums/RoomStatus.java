package com.zylear.netty.learn.enums;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public enum RoomStatus {

    unknown(-1),
    waiting(1),
    gaming(2)
    ;

    private Integer value;

    RoomStatus(Integer value) {
        this.value=value;
    }

    public Integer getValue() {
        return value;
    }

    public static RoomStatus valueOf(Integer value) {
        try {
            for (RoomStatus roomStatus : RoomStatus.values()) {
                if (roomStatus.getValue().equals(value)) {
                    return roomStatus;
                }
            }
        } catch (Exception e) {
        }
        return unknown;
    }
}
