package com.zylear.netty.learn.enums;

/**
 * Created by xiezongyu on 2018/2/5.
 */
public enum ChooseColor {

    unknown(-1),
    blue(1),
    green(2),
    yellow(3),
    red(4);

    private Integer value;

    ChooseColor(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static ChooseColor valueOf(Integer value) {
        try {
            for (ChooseColor chooseColor : ChooseColor.values()) {
                if (chooseColor.getValue().equals(value)) {
                    return chooseColor;
                }
            }
        } catch (Exception e) {
        }
        return unknown;
    }
}
