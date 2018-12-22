package com.viscu.curator.checkConfig;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryNTimes;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ zk 统一更新配置
 */
public class Client3 extends Client{

    public Client3() {
        zkServerPath = "localhost:2283";
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }

    public static void main(String[] args) throws Exception {
        Client3 cto = new Client3();
        System.out.println("client3 启动成功...");

        final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, CONFIG_NODE_PATH, true);
        childrenCache.start( PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        // 添加监听事件
        ClientSolve.solve(childrenCache, CONFIG_NODE_PATH, SUB_PATH);
        latch.await();
        cto.closeZKClient();
    }


}
