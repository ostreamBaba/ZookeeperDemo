package com.viscu.zk.demo;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 使用ip方式的acl
 */


public class ZKTest {

    //只有在指定ip的机子上才可以访问该节点
    @Test
    public void testByIp(){
        //本机ip是127.0.0.1
        ZKNodeAcl zkServer = new ZKNodeAcl(ZKConfig.zkServerPathSingle);
        List<ACL> aclsIp = new ArrayList<>();
        //Id ipId1 = new Id("ip", "172.31.41.225");
        Id ipId1 = new Id("ip", "127.0.0.1");
        aclsIp.add(new ACL( ZooDefs.Perms.ALL, ipId1));
        zkServer.createZKNode("/acl/iptest1", "123".getBytes(), aclsIp);
        //本地客户端是可以对
    }

}
