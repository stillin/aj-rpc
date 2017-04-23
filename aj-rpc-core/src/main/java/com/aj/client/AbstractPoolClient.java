package com.aj.client;

import com.aj.exception.RpcException;
import com.aj.transport.Channel;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * channel pool
 * Created by chaiaj on 2017/4/8.
 */
public abstract class AbstractPoolClient extends AbstractClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPoolClient.class);
    protected GenericObjectPool<Object> pool;

    protected void initPool() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(10);

        PooledObjectFactory factory = createChannelFactory();
        pool = new GenericObjectPool<Object>(factory, poolConfig);

        for (int i = 0; i < poolConfig.getMinIdle(); i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                LOGGER.error("init pool failed when add object", e);
                e.printStackTrace();
            }
        }
    }

    protected Channel borrowObject() throws Exception {
        Channel channel = (Channel) pool.borrowObject();
        if (channel == null) {
            invalidateObject(channel);
            String error = "borrow channel from pool failed, url:";
            LOGGER.error(error);
            throw new RpcException(error);
        }
        return channel;
    }

    protected void returnObject(Channel channel) {
        pool.returnObject(channel);
    }

    protected void invalidateObject(Channel channel) {
        try {
            pool.invalidateObject(channel);
        } catch (Exception e) {
            LOGGER.error("invalidate object failed");
            e.printStackTrace();
        }
    }

    protected abstract BasePooledObjectFactory createChannelFactory();
}

