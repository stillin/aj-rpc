package com.aj.app;

import com.aj.service.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chaiaj on 2017/4/19.
 */
public class HelloApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"consumer.xml"});
        HelloService helloService = (HelloService) context.getBean("helloService");
        String result = helloService.hello("world");
        System.out.println(result);
    }
}
