package com.aj.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chaiaj on 2017/4/18.
 */
public class Provider {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"provider.xml"});
        //Thread.sleep(Long.MAX_VALUE);
    }
}
