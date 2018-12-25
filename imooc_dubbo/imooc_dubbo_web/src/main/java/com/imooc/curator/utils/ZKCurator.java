package com.imooc.curator.utils;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ Create by ostreamBaba on 18-12-25
 * @ 描述
 */
public class ZKCurator {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZKCurator.class);

    private CuratorFramework client = null;

    public ZKCurator(CuratorFramework client) {
        this.client = client;
    }

    /*初始化方法*/
    public void init(){
        client = client.usingNamespace("zk-curator-connector");
    }

    /*判断zk是否连接*/
    public boolean isZKAlive(){
        return client.isStarted();
    }

}
