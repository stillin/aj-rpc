package com.aj.common;

import java.io.Serializable;

/**
 * Created by chaiaj on 2017/4/2.
 */
public class RpcResponse implements Serializable {
    private String requestId;
    private Object result;
    private Throwable exception;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean isError() {
        return exception != null;
    }
}
