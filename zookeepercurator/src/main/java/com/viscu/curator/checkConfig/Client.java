package com.viscu.curator.checkConfig;

import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.CountDownLatch;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class Client {

    public CuratorFramework client = null;

    public static String zkServerPath = null;

    public final static String CONFIG_NODE_PATH = "/super/node";

    public final static String SUB_PATH = "/redis-config";

    public static CountDownLatch latch = new CountDownLatch(1);

    protected void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }
}
