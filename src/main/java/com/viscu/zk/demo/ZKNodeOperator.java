package com.viscu.zk.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 同步或者异步创建zknode节点
 */

public class ZKNodeOperator implements Watcher{

    private ZooKeeper zooKeeper = null;

    public ZKNodeOperator() { }

    public ZKNodeOperator(String connectString){
        try {
            zooKeeper = new ZooKeeper(connectString, ZKConfig.timeout, new ZKNodeOperator());

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

    //创建zk节点
    //acl d w r c a是可以修改权限 w是写权限 r是读权限 c的创建节点的权限 d是删除权限
    //同步或者异步创建节点
    public void createZKNode(String path, byte[] data, List<ACL> acls){
        String result = "";
        /**
         *  参数：
         *  path： 创建路径
         *  data： 节点存储的数据 不大 一般只要几k
         *  acl： 控制权限策略
         *  createMode： 节点的类型
         *  PERSISTENT：持久节点 0x0
         * 	PERSISTENT_SEQUENTIAL：持久顺序节点
         * 	EPHEMERAL：临时节点
         * 	EPHEMERAL_SEQUENTIAL：临时顺序节点
         */

        try {
            //同步创建节点
            //result = zooKeeper.create(path, data, acls, CreateMode.EPHEMERAL);
            //创建临时节点 心跳机制检测并会删除临时节点(会话结束)

            //异步创建节点
            //ctx可以用来进行额外的操作 例如 传递创建成功的消息
            String ctx = "{'create:'success'}";
            //异步创建节点
            zooKeeper.create(path, data, acls, CreateMode.PERSISTENT, new CreateCallBack(), ctx);

            System.out.println("创建节点：" + result + "成功");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //版本号 乐观锁
    public Stat set(String path, byte[] data, int version){
        Stat status = null;
        try {
            status = zooKeeper.setData(path, data, version);
        } catch (KeeperException | InterruptedException e) {
            System.out.println("版本号不正确");
        }
        return status;
    }

    public static void main(String[] args) throws InterruptedException {
        ZKNodeOperator zkServer = new ZKNodeOperator(ZKConfig.zkServerPathSingle);
        //创建zk节点

        //zkServer.createZKNode("/node1", "node1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
        //ZooDefs.Ids.OPEN_ACL_UNSAFE = world:anyone:cdrwa

        //同步修改节点的数据 异步和创建相同
        //Stat status = zkServer.set("/node1", "abc".getBytes(), 0);
        //System.out.println(status.getVersion());
        //Stat同客户端stat命令

        //异步删除节点
        zkServer.delete("/node1", 1);
        Thread.sleep(1000);
    }

    public void delete(String path, int version){
        String ctx = "{'delete':'success'}";
        //同步删除
        /*try {
            zooKeeper.delete(path, version);
        } catch (Exception e) {
            e.printStackTrace();
        } */
        //异步删除
        zooKeeper.delete(path, version, new DeleteCallBack(), ctx);
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //todo
    }
}
