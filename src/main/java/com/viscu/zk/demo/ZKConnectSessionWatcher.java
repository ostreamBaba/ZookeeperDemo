package com.viscu.zk.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @ Create by ostreamBaba on 18-12-21
 * @ zookeeper 恢复之前的会话连接
 */

public class ZKConnectSessionWatcher implements Watcher{

    public static final Logger LOGGER = LoggerFactory.getLogger(ZKConnectSessionWatcher.class);

    //单机版本
    public static final String zkServerPath = "127.0.0.1:2181";

    //集群版本
    //public static final String zkServerPath = "127.0.0.1:2281,127.0.0.1:2282,127.0.0.1:2283";

    public static final Integer timeout = 5000;

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZKConnectSessionWatcher());
        LOGGER.debug("客户端开始连接zookeeper服务器...");
        LOGGER.debug("连接状态： {}", zk.getState()); //连接中
        Thread.sleep(1000); //设置暂停 zk连接需要一定时间
        long sessionId = zk.getSessionId(); //获取会话id
        byte[] sessionPasswd = zk.getSessionPasswd(); //获取会话的密码
        String ssid = "0x" + Long.toHexString(sessionId);
        System.out.println(ssid);
        //System.out.println(sessionId);

        LOGGER.debug("连接状态： {}", zk.getState()); //已经连接


        Thread.sleep(200);

        //开始会话重连
        LOGGER.debug("开始进行会话重连");

        ZooKeeper zkSession = new ZooKeeper(zkServerPath,
                timeout,
                new ZKConnectSessionWatcher(),
                sessionId,
                sessionPasswd);

        LOGGER.debug("重新连接状态zkSession: {}", zkSession.getState());
        Thread.sleep(1000);
        LOGGER.debug("重新连接状态zkSession: {}", zkSession.getState());

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        LOGGER.debug("接收到watch通知: {}", watchedEvent);
    }
}