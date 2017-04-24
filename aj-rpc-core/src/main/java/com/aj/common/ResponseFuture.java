package com.aj.common;

import com.aj.exception.RpcException;
import com.aj.transport.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chaiaj on 2017/4/8.
 */
public class ResponseFuture implements Future {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResponseFuture.class);

    private static final Map<String, ResponseFuture> FUTURES = new ConcurrentHashMap<String, ResponseFuture>();
    private Channel channel;
    private RpcRequest request;
    private volatile RpcResponse response;
    private Object lock = new Object();
    private long createTime;
    private int timeout;

    public ResponseFuture(RpcRequest request, Channel channel, int timeout) {
        this.request = request;
        this.channel = channel;
        createTime = System.currentTimeMillis();
        this.timeout = timeout;
    }

    public static void registerFuture(String requestId, ResponseFuture future) {
        FUTURES.put(requestId, future);
    }

    public static ResponseFuture removeFuture(String requestId) {
        return FUTURES.remove(requestId);
    }

    public static ResponseFuture getFuture(String requestId) {
        return FUTURES.get(requestId);
    }

    public boolean cancel() {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return response != null;
    }

    public Object get() {
        return get(timeout);
    }

    public Object get(int timeout) {
        synchronized (lock) {
            if (timeout <= 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RpcException("ResponseFuture get response failed, request:" + response.getRequestId()
                            + ", cost:" + (System.currentTimeMillis() - createTime));
                    //LOGGER.warn();
                }
                return response;
            } else {
                int waitTime = (int) (timeout - (System.currentTimeMillis() - createTime));
                if (waitTime > 0) {
                    for (;;) {
                        try {
                            lock.wait(waitTime);
                        } catch (InterruptedException e) {
                            throw new RpcException("ResponseFuture get response failed, request:" + request
                                    + ", cost:" + (System.currentTimeMillis() - createTime));
                        }

                        if (!isDone()) {
                            throw new RpcException("ResponseFuture get response timeout, request:"
                                    + request);
                        }
                        return response;
                    }
                }

                if (!isDone()) {
                    throw new RpcException("ResponseFuture get response timeout, request:"
                            + request);
                }
            }
        }
        return null;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
