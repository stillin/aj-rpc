package com.aj.exception;

/**
 * Created by chaiaj on 2017/4/8.
 */
public class RpcException extends RuntimeException {
    private String message;

    public RpcException(String message) {
        this.message = message;
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
