package com.viscu.curator.checkConfig;

import com.viscu.curator.utils.JsonUtils;
import com.viscu.curator.utils.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class Client2 extends Client{

    public Client2() {
        zkServerPath = "localhost:2282";
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }

    public static void main(String[] args) throws Exception {
        Client2 cto = new Client2();
        System.out.println("client2 启动成功...");

        final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, CONFIG_NODE_PATH, true);
        childrenCache.start( PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        // 添加监听事件
        ClientSolve.solve(childrenCache, CONFIG_NODE_PATH, SUB_PATH);
        latch.await();
        cto.closeZKClient();
    }


}