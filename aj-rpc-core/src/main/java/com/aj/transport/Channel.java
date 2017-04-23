package com.aj.transport;

import com.aj.common.RpcRequest;
import com.aj.common.RpcResponse;

/**
 * Created by chaiaj on 2017/4/8.
 */
public interface Channel {
    RpcResponse request(RpcRequest request);

    boolean open();

    boolean close();
}
