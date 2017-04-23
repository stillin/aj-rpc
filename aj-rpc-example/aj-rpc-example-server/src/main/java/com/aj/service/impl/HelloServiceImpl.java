package com.aj.service.impl;

import com.aj.annotation.RpcService;
import com.aj.service.HelloService;

/**
 * Created by chaiaj on 2017/4/17.
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "hello " + name;
    }
}
