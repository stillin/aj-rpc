package com.aj.transport.netty;

import com.aj.client.AbstractPoolClient;
import com.aj.common.RpcRequest;
import com.aj.common.RpcResponse;
import com.aj.common.URL;
import com.aj.exception.RpcException;
import com.aj.transport.Channel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chaiaj on 2017/4/2.
 */
public class NettyClient extends AbstractPoolClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
    private Bootstrap bootstrap;

    public RpcResponse request(RpcRequest request) {
        Channel channel = null;
        RpcResponse response = null;
        try {
            channel = borrowObject();
            if (channel == null) {
                return null;
            }
            response = channel.request(request);
            returnObject(channel);
        } catch (Exception e) {
            invalidateObject(channel);
            if (e instanceof RpcException) {
                throw new RpcException(e);
            } else {
                throw new RpcException("nettyClient request failed, request:" + request.getRequestId()
                    + ", url:" + url);
            }
        }
        return response;
    }

    public boolean open() {
        initBootStrap();
        initPool();
        return true;
    }

    public boolean close() {
        pool.close();
        return true;
    }

    private void initBootStrap() {
        EventLoopGroup group = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new NettyDecoder(serializer, RpcResponse.class));
                        ch.pipeline().addLast("encoder", new NettyEncoder(serializer, RpcRequest.class));
                        ch.pipeline().addLast("handler", new NettyChannelHandler());
                    }
                });
    }

    @Override
    protected BasePooledObjectFactory createChannelFactory() {
        return new NettyChannelFactory(this);
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public URL getUrl() {
        return url;
    }
}
