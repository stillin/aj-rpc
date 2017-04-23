package com.aj.transport.netty;

import com.aj.common.ResponseFuture;
import com.aj.common.RpcRequest;
import com.aj.common.RpcResponse;
import com.aj.common.URL;
import com.aj.exception.RpcException;
import com.aj.transport.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created by chaiaj on 2017/4/8.
 */
public class NettyChannel implements Channel {
    private NettyClient nettyClient;
    private io.netty.channel.Channel channel;

    public NettyChannel(NettyClient client) {
        this.nettyClient = client;
    }

    public RpcResponse request(RpcRequest request) {
        ResponseFuture responseFuture = new ResponseFuture(request, this, request.getTimeout());
        ResponseFuture.registerFuture(request.getRequestId(), responseFuture);
        try {
            this.channel.writeAndFlush(request).sync();
            return (RpcResponse) responseFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RpcException("io.netty.channel.Channel write request failed");
        } catch (RpcException e) {
            throw e;
        } finally {
            ResponseFuture.removeFuture(request.getRequestId());
        }
    }

    public synchronized boolean open() {
        URL url = nettyClient.getUrl();
        ChannelFuture channelFuture = null;
        try {
            channelFuture = nettyClient.getBootstrap().connect(url.getHost(), url.getPort()).sync();
            if (channelFuture.isSuccess()) {
                this.channel = channelFuture.channel();
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public synchronized boolean close() {
        if (this.channel != null) {
            if (this.channel.isOpen()) {
                this.channel.close();
            }
        }
        return true;
    }
}
