package com.zylear.netty.learn.enums;

/**
 * Created by xiezongyu on 2018/2/5.
 */
public enum BlokusColor {

    unknown(-1),
    blue(1),
    green(2),
    red(3),
    yellow(4);


    private Integer value;

    BlokusColor(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static BlokusColor valueOf(Integer value) {
        try {
            for (BlokusColor blokusColor : BlokusColor.values()) {
                if (blokusColor.getValue().equals(value)) {
                    return blokusColor;
                }
            }
        } catch (Exception e) {
        }
        return unknown;
    }
}
