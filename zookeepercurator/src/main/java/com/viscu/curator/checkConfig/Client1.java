package com.viscu.curator.checkConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryNTimes;


/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class Client1 extends Client{

    public Client1() {
        zkServerPath = "localhost:2281";
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }

    public static void main(String[] args) throws Exception {
        Client1 cto = new Client1();
        System.out.println("client1启动成功... ");

        final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, CONFIG_NODE_PATH, true);
        childrenCache.start( PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        //添加监听事件
        ClientSolve.solve(childrenCache, CONFIG_NODE_PATH, SUB_PATH);
        latch.await();
        cto.closeZKClient();
    }


}
