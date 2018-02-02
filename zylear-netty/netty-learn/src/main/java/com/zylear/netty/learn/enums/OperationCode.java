package com.zylear.netty.learn.enums;

/**
 * Created by xiezongyu on 2018/2/2.
 */
public enum OperationCode {

    unknown((short) 10000),
    check_version((short) 1),
    login((short) 2),
    create_room((short) 3),
    join_room((short) 4),
    ;

    private Short value;

    OperationCode(Short value) {
        this.value = value;
    }

    public Short getValue() {
        return value;
    }
}
