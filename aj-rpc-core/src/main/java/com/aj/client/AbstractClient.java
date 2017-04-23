package com.aj.client;

import com.aj.common.RpcRequest;
import com.aj.common.RpcResponse;
import com.aj.common.URL;
import com.aj.serialize.Serializer;

/**
 * Created by chaiaj on 2017/4/2.
 */
public abstract class AbstractClient implements Client {
    // 服务地址
    protected URL url;
    protected Serializer serializer;

    protected int timeout;

    public void init(URL url, Serializer serializer, int timeout) {
        this.url = url;
        this.serializer = serializer;
        this.timeout = timeout;
    }


    public Serializer getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
