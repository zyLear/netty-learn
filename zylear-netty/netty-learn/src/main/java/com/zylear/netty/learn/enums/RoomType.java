package com.zylear.netty.learn.enums;

/**
 * @author 28444
 * @date 2018/2/4.
 */
public enum RoomType {
    unknown(-2),
    blokus_two(1),
    block_four(2);

    private Integer value;

    RoomType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static RoomType valueOf(Integer value) {
        try {
            for (RoomType roomType : RoomType.values()) {
                if (roomType.getValue().equals(value)) {
                    return roomType;
                }
            }
        } catch (Exception e) {
        }
        return unknown;
    }
}
