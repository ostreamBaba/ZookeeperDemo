package com.viscu.zk.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @ Create by ostreamBaba on 18-12-21
 * @ zookeeper 连接Demo演示
 */

public class ZKConnect implements Watcher{

    public static final Logger LOGGER = LoggerFactory.getLogger(ZKConnect.class);

    //单机版本
    //public static final String zkServerPath = "127.0.0.1:2281";

    //集群版本
    public static final String zkServerPath = "127.0.0.1:2281,127.0.0.1:2282,127.0.0.1:2283";

    public static final Integer timeout = 5000;
    public static void main(String[] args) throws IOException, InterruptedException {
        //客户端和zk服务端链接是一个异步的过程
        //当连接成功后后，客户端会收的一个watch通知
        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZKConnect());
        //服务端地址 超时时间 所触发的watcher事件

        LOGGER.debug("客户端开始连接zookeeper服务器...");
        LOGGER.debug("连接状态： {}", zk.getState()); //连接中
        Thread.sleep(2000); //设置暂停 zk连接需要一定时间
        LOGGER.debug("连接状态： {}", zk.getState()); //已经连接
    }


    //watcher事件有多个 针对父节点的增删改事件 针对子节点的增删事件(NodeChanged) 改不触发watcher事件
    @Override
    public void process(WatchedEvent watchedEvent) {
        LOGGER.debug("接收到watch通知: {}", watchedEvent);
    }
}
