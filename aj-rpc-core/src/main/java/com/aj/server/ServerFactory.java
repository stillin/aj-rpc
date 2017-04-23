package com.aj.server;

import com.aj.annotation.RpcService;
import com.aj.common.RpcRequest;
import com.aj.common.RpcResponse;
import com.aj.serialize.Hessian2Serializer;
import com.aj.serialize.Serializer;
import com.aj.transport.netty.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chaiaj on 2017/4/3.
 */
public class ServerFactory implements InitializingBean, ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerFactory.class);

    private int port;
    private Serializer serializer;

    private Server server;

    private static final Map<String, Object> serviceMap = new HashMap<String, Object>();

    public void afterPropertiesSet() throws Exception {
        server = new NettyServer();
        if (serializer == null) {
            serializer = new Hessian2Serializer();
        }
        server.start(port, serializer);
    }


    public static RpcResponse invoke(RpcRequest request) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());

        Object serviceBean = serviceMap.get(request.getClassName());
        Class serviceClass = serviceBean.getClass();
        Object result = null;
        try {
            Method method = serviceClass.getMethod(request.getMethodName(), request.getParameterTypes());
            result = method.invoke(serviceBean, request.getParameters());
            response.setResult(result);
        } catch (Throwable e) {
            response.setException(e);
            e.printStackTrace();
        }
        return response;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (beanMap != null) {
            for (Object bean : beanMap.values()) {
                String serviceName = bean.getClass().getAnnotation(RpcService.class).value().getName();
                serviceMap.put(serviceName, bean);
            }
        }
    }
}
