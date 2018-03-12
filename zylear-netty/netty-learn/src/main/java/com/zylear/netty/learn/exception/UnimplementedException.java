package com.zylear.netty.learn.exception;

import java.io.IOException;

/**
 * Created by xiezongyu on 2018/3/10.
 */
public class UnimplementedException extends RuntimeException {

    private static final String msg = "unimplemented exception! ";

    public UnimplementedException() {
    }

    public UnimplementedException(String message) {
        super(msg + message);
    }

    public UnimplementedException(String message, Throwable cause) {
        super(msg + message, cause);
    }

    public UnimplementedException(Throwable cause) {
        super(cause);
    }

}
