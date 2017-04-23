package com.aj.client;

import com.aj.common.URL;
import com.aj.serialize.Serializer;
import com.aj.transport.Channel;

/**
 * Created by chaiaj on 2017/4/2.
 */
public interface Client extends Channel {
    void init(URL url, Serializer serializer, int timeout);
}
