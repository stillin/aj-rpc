package com.aj.client;

import com.aj.common.RpcConstant;
import com.aj.common.RpcRequest;
import com.aj.common.RpcResponse;
import com.aj.common.URL;
import com.aj.exception.RpcException;
import com.aj.serialize.Hessian2Serializer;
import com.aj.serialize.Serializer;
import com.aj.transport.netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by chaiaj on 2017/4/3.
 */
public class ClientFactory implements InitializingBean, FactoryBean<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientFactory.class);
    protected String host;
    protected int port;
    protected String interfaceName;
    private int timeout = RpcConstant.DEFAULT_TIMEOUT;

    private Serializer serializer;
    private Client client;

    public void afterPropertiesSet() throws Exception {
        client = new NettyClient();
        if (serializer == null) {
            serializer = new Hessian2Serializer();
        }
        client.init(new URL(host, port), serializer, timeout);
        client.open();
    }

    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{Class.forName(interfaceName)},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        request.setCreateTime(System.currentTimeMillis());
                        request.setTimeout(timeout);

                        RpcResponse response = client.request(request);
                        if (response == null) {
                            LOGGER.warn("response received is null, request:" + request.getRequestId());
                            throw new RpcException("response is null");
                        }
                        if (response.isError()) {
                            throw new RpcException(response.getException());
                        }

                        return response.getResult();
                    }
                });
    }

    public Class<?> getObjectType() {
        try {
            return Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            LOGGER.error("class not found:" + interfaceName);
            e.printStackTrace();
        }
        return null;
    }

    public boolean isSingleton() {
        return false;
    }



    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }
}
