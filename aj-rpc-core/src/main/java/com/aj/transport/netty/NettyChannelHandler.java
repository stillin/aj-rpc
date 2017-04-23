package com.aj.transport.netty;


import com.aj.common.ResponseFuture;
import com.aj.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * channel handler on client side
 * Created by chaiaj on 2017/4/9.
 */
public class NettyChannelHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyChannelHandler.class);


    public NettyChannelHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {

        ResponseFuture responseFuture =  ResponseFuture.getFuture(response.getRequestId());
        if (responseFuture == null) {
            LOGGER.warn("get responseFuture failed, request:" + response.getRequestId());
            return;
        }
        responseFuture.setResponse(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOGGER.warn("NettyClientHandler catch exception:" + cause);
    }
}
