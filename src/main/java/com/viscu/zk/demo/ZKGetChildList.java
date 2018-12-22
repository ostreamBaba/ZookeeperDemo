package com.viscu.zk.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 获取子节点列表
 */
public class ZKGetChildList implements Watcher{

    private ZooKeeper zooKeeper = null;

    public ZKGetChildList() { }

    public ZKGetChildList(String connectString){
        try {
            zooKeeper = new ZooKeeper(connectString, ZKConfig.timeout, new ZKGetChildList());
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

    public static CountDownLatch latch = new CountDownLatch(1);

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public static void main(String[] args) {
        ZKGetChildList zkServer = new ZKGetChildList(ZKConfig.zkServerPathSingle);

        try {
            /*List<String> childList = zkServer.getZooKeeper().getChildren("/viscu", true);
            //参数 父节点路径 注册一个watch事件
            for (String child : childList){
                System.out.println(child);
            }*/

            //异步调用 异步获取数据
            String ctx = "{'callback':'childCallBack'}";
            //zkServer.getZooKeeper().getChildren("/viscu", true, new ChildrenCallBack(), ctx);
            zkServer.getZooKeeper().getChildren("/viscu", true, new Children2CallBack(), ctx);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            if(event.getType()== Event.EventType.NodeChildrenChanged){
                System.out.println("NodeChildrenChanged");
                ZKGetChildList zkServer = new ZKGetChildList(ZKConfig.zkServerPathSingle);
                List<String> strChildList = zkServer.getZooKeeper().getChildren(event.getPath(), false);
                for (String s : strChildList) {
                    System.out.println(s);
                }
                latch.countDown();
            } else if(event.getType() == Event.EventType.NodeCreated) {
                System.out.println("NodeCreated");
            } else if(event.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("NodeDataChanged");
            } else if(event.getType() == Event.EventType.NodeDeleted) {
                System.out.println("NodeDeleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
