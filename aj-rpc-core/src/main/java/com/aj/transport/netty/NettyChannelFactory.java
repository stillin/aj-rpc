package com.aj.transport.netty;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by chaiaj on 2017/4/9.
 */
public class NettyChannelFactory extends BasePooledObjectFactory<NettyChannel> {
    private NettyClient nettyClient;
    public NettyChannelFactory(NettyClient nettyClient) {
        super();
        this.nettyClient = nettyClient;
    }

    @Override
    public NettyChannel create() throws Exception {
        NettyChannel nettyChannel = new NettyChannel(nettyClient);
        nettyChannel.open();
        return nettyChannel;
    }

    @Override
    public PooledObject<NettyChannel> wrap(NettyChannel nettyChannel) {
        return new DefaultPooledObject<NettyChannel>(nettyChannel);
    }

    @Override
    public void destroyObject(PooledObject<NettyChannel> p) throws Exception {
        NettyChannel nettyChannel = p.getObject();
        if (nettyChannel != null) {
            nettyChannel.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<NettyChannel> p) {
        return super.validateObject(p);
    }

    @Override
    public void activateObject(PooledObject<NettyChannel> p) throws Exception {
        super.activateObject(p);
    }
}
