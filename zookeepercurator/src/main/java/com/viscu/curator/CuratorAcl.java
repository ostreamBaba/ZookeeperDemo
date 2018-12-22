package com.viscu.curator;

import com.viscu.curator.utils.AclUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class CuratorAcl {

    public CuratorFramework client = null;
    public static final String zkServerPath = "localhost:2181";

    public CuratorAcl() {
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        //先进行认证
        client = CuratorFrameworkFactory.builder().authorization("digest", "viscu1:123456".getBytes())
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }

    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    public static void main(String[] args) throws Exception {
        CuratorAcl cto = new CuratorAcl();
        boolean isStarted = cto.client.isStarted();
        System.out.println("当前客户端的状态： " + (isStarted?"连接中":"已关闭"));

        List<ACL> acls = new ArrayList<>();
        Id viscu1 = new Id("digest", AclUtils.getDigestUserPwd("viscu1:123456"));
        Id viscu2 = new Id("digest", AclUtils.getDigestUserPwd("viscu2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL, viscu1)); //用户1拥有全部权限
        acls.add(new ACL(ZooDefs.Perms.READ, viscu2)); //用户2只有读权限
        acls.add(new ACL(ZooDefs.Perms.CREATE | ZooDefs.Perms.DELETE, viscu2)); //给用户2添加创建权限和删除权限


        /*String nodePath = "/acl/father/child/sub/1/2/3"; //递归取创建子节点 需要拥有对应的权限才可以进行递归的创建
        byte[] data = "spiderman".getBytes();
        cto.client.create().creatingParentsIfNeeded()
                .withMode( CreateMode.PERSISTENT)
                .withACL( acls, true) //true 递归赋予权限
                .forPath(nodePath, data);

        Thread.sleep(2000);*/

        //进行权限的膝盖
        //cto.client.setACL().withACL(acls).forPath("/curatorNode");



    }

}
