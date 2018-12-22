package com.viscu.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.*;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */

public class CuratorOperator {

    public CuratorFramework client = null;

    public static final String zkServerPath = "127.0.0.1:2181";

    public CuratorOperator() {
        //同步创建zk示例, 原生api是异步的

        //参数
        // baseSleepTimeMs： 初始sleep时间(两次重试的间隔时间)
        // maxRetries： 最大重试次数(超过最大重试次数则不继续尝试)
        // maxSleepMs: 最大重试时间
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        //参数
        // n: 重试的次数
        // sleepMsBetweenRetries: 每次重试间隔时间
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);

        //参数：sleepMsBetweenRetry: 每次重试间隔时间
        //重试一次
        //RetryPolicy retryPolicy2 = new RetryOneTime(3000);

        //一直重试 不停止 不推荐使用 消耗资源(出现宕机情况)
        //RetryPolicy retryPolicy3 = new RetryForever(retryIntervalMs);

        //参数：
        //maxElapsedTimeMs: 最大重试时间
        //sleepMsBetweenRetries 每次重试间隔
        //重试时间超过maxElapsedTimeMs后 就不进行重试
        //RetryPolicy retryPolicy3 = new RetryUntilElapsed(2000, 3000);


        //实例化客户端
        //在原生zk api的基础上加上自动重试机制
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("workspace") //建立workspace 所有操作都会在这个节点上进行操作
                .build();
        client.start();
    }

    public void closeZKClient(){
        if(client != null){
            client.close();
        }
    }

    public static void main(String[] args) throws Exception {
        CuratorOperator cto = new CuratorOperator();
        boolean isStarted = cto.client.isStarted();
        System.out.println("当前客户端的状态： " + (isStarted?"连接中":"已经关闭"));
        Thread.sleep(3000);
        cto.closeZKClient();
        isStarted = cto.client.isStarted();
        System.out.println("当前客户端的状态： " + (isStarted?"连接中":"已经关闭"));
    }

}
