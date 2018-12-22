package com.viscu.zk.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class ZKNodeExist implements Watcher{

    private ZooKeeper zooKeeper = null;

    private static CountDownLatch latch = new CountDownLatch(1);

    public ZKNodeExist() { }

    public ZKNodeExist(String connectString){
        try {
            zooKeeper = new ZooKeeper(connectString, ZKConfig.timeout, new ZKNodeExist());
        } catch (IOException e) {
            e.printStackTrace();
            if(zooKeeper != null){
                try {
                    zooKeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType() == Event.EventType.NodeCreated){
            System.out.println("节点创建");
            latch.countDown();
        }else if(event.getType() == Event.EventType.NodeDataChanged){
            System.out.println("节点数据改变");
            latch.countDown();
        }else if(event.getType() == Event.EventType.NodeDeleted){
            System.out.println("节点删除");
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        ZKNodeExist zkServer = new ZKNodeExist(ZKConfig.zkServerPathSingle);

        try {
            Stat stat = zkServer.getZooKeeper().exists("/viscu", true);
            if(stat != null){
                System.out.println("查询节点的版本为dataVersion: " + stat.getVersion());
            }else {
                System.out.println("该节点不存在");
            }
            latch.await();
        } catch (Exception e) {
            //todo
        }
    }

}
