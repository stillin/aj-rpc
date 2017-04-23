package com.aj.server;

import com.aj.common.URL;
import com.aj.serialize.Serializer;

/**
 * Created by chaiaj on 2017/4/2.
 */
public interface Server {
    void start(int port, Serializer serializer);

    void close();
}
