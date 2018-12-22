package com.viscu.zk.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ zk操作节点acl
 */

public class ZKNodeAcl implements Watcher{

    private ZooKeeper zooKeeper = null;

    private static CountDownLatch latch = new CountDownLatch(1);

    public ZKNodeAcl() { }

    public ZKNodeAcl(String connectString){
        try {
            zooKeeper = new ZooKeeper(connectString, ZKConfig.timeout, new ZKNodeAcl());
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

    public void createZKNode(String path, byte[] data, List<ACL> acls){
        String result = "";
        try {
            //同步创建节点
            result = zooKeeper.create(path, data, acls, CreateMode.PERSISTENT);
            System.out.println("创建节点：" + result + "成功");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }


    @Override
    public void process(WatchedEvent event) {

    }

    //! zk不可以递归创建节点
    public static void main(String[] args) throws NoSuchAlgorithmException, KeeperException, InterruptedException {
        ZKNodeAcl zkServer = new ZKNodeAcl(ZKConfig.zkServerPathSingle);
        //acl 任何人都可以访问
        //zkServer.createZKNode("/acl", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);

        //自定义用户认证访问 定义两个用户
        /*List<ACL> acls = new ArrayList<>();
        Id viscu1 = new Id("digest", AclUtils.getDigestUserPwd("viscu1:123456"));
        Id viscu2 = new Id("digest", AclUtils.getDigestUserPwd("viscu2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL, viscu1)); //用户1拥有全部权限
        acls.add(new ACL(ZooDefs.Perms.READ, viscu2)); //用户2只有读权限
        acls.add(new ACL(ZooDefs.Perms.CREATE | ZooDefs.Perms.DELETE, viscu2)); //给用户2添加创建权限和删除权限
        zkServer.createZKNode("/acl/testdigest", "testDigest".getBytes(), acls);*/

        //注册过的用户必须通过addAuthInfo才能操作节点
        zkServer.getZooKeeper().addAuthInfo("digest", "viscu1:123456".getBytes());
        zkServer.createZKNode("/acl/testdigest/childnode", "child".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL);
        //读取操作
        Stat stat = new Stat();
        byte[] data = zkServer.getZooKeeper().getData("/acl/testdigest", false, stat);
        System.out.println(new String(data));
        //读取操作 viscu1用户是没有修改权限的 所以会抛出异常
        zkServer.getZooKeeper().setData("/acl/testdigest", "123456".getBytes(), 3);
    }

}
