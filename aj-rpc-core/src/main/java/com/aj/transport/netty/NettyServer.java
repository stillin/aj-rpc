package com.aj.transport.netty;

import com.aj.common.RpcRequest;
import com.aj.common.RpcResponse;
import com.aj.serialize.Serializer;
import com.aj.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chaiaj on 2017/4/13.
 */
public class NettyServer implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private Channel channel;

    public void start(int port, final Serializer serializer) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_TIMEOUT, 100)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new NettyDecoder(serializer, RpcRequest.class))
                                    .addLast(new NettyEncoder(serializer, RpcResponse.class))
                                    .addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            LOGGER.info("===server start success, port={}", port);
            channel = future.channel().closeFuture().sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void close() {
        if (channel != null) {
            if (channel.isOpen()) {
                channel.close();
            }
        }
    }
}
