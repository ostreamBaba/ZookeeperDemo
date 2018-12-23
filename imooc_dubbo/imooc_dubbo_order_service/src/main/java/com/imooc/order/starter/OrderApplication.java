package com.imooc.order.starter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @ Create by ostreamBaba on 18-12-23
 * @ 通过Main函数启动dubbo服务
 */
public class OrderApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/spring-context.xml"}
        );
        context.start();
        System.in.read();
    }

}
