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
public class ZKGetNodeData implements Watcher{

    private ZooKeeper zooKeeper = null;

    private static Stat stat = new Stat();

    public ZKGetNodeData() { }

    public ZKGetNodeData(String connectString){
        try {
            zooKeeper = new ZooKeeper(connectString, ZKConfig.timeout, new ZKGetNodeData());
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

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {
        ZKGetNodeData zkServer = new ZKGetNodeData(ZKConfig.zkServerPathSingle);
        try {
            //true/false 注册一个watcher事件
            //设置true表示对当前事件进行监听
            //设置stat会获取相关的节点信息
            //命令行改变/viscu的数据 由于设置监听 会触发zookeeper的监听事件(ZKGetNodeData);
            byte[] resByte = zkServer.getZooKeeper().getData("/viscu", true, stat);
            String result = new String(resByte);
            System.out.println("当前值： " + result);
            System.out.println("当前的版本： " + stat.getVersion());
            latch.await(); //等待获取节点数据完成
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            //若节点数据发送变化
            if (watchedEvent.getType() == Event.EventType.NodeDataChanged){
                ZKGetNodeData zkServer = new ZKGetNodeData(ZKConfig.zkServerPathSingle);
                byte[] resByte = null;
                resByte = zkServer.getZooKeeper().getData("/viscu", false, stat);
                String result = new String(resByte);
                System.out.println("更改后的值： " + result);
                System.out.println("版本号变化DataVersion： " + stat.getVersion());
                latch.countDown();
            } else if(watchedEvent.getType() == Event.EventType.NodeCreated){
                //todo
            } else if(watchedEvent.getType() == Event.EventType.NodeDeleted){
                //todo
            } else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                //todo
            }
        }catch (Exception e){

        }
    }
}
