package com.imooc.curator.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @ Create by ostreamBaba on 18-12-25
 * @ 分布式锁的实现工具类
 */
public class DistributedLock {

    private CuratorFramework client = null;

    public static final Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);

    // 用于挂起当前请求, 并等待一个分布式锁的释放
    private static CountDownLatch zkLocklatch = new CountDownLatch(1);

    //分布式节点的总节点名 多个业务
    private static final String ZK_LOCK_PROJECT = "all_locks";

    //分布式锁节点
    private static final String DISTRIBUTED_LOCK = "distributed_lock";

    public DistributedLock(CuratorFramework client) {
        this.client = client;
    }

    public void init(){
        //使用命名空间
        client = client.usingNamespace("ZKLocks-Namespace");

        /**
         *  创建zk锁的总节点
         *  ZKLocks-Namespace
         *      |
         *       —— all_locks //所有的锁都会创建在all_locks父节点之下
         *              |
         *               —— distributed_lock
         */

        try {
            if(client.checkExists().forPath("/" + ZK_LOCK_PROJECT) == null){
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode( CreateMode.PERSISTENT)
                        .withACL( ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK_PROJECT);
            }
            //针对zk的分布式锁节点 创建相应的watcher事件监听
            addWatcherToLock("/" + ZK_LOCK_PROJECT);
            //对ZK_LOCK_PROJECT下的子节点进行监听
        } catch (Exception e) {
            LOGGER.error("客户端连接zk服务器错误...请重试...");
        }
    }


    // 创建watcher监听
    private void addWatcherToLock(String path) throws Exception {
        final PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener((client, event)->{
            //如果该节点被删除
            if(event.getType().equals( PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                String nodePath = event.getData().getPath();
                LOGGER.info("上一个会话已释放锁或者该会断断开, 节点路径为: " + nodePath);
                if(nodePath.contains(DISTRIBUTED_LOCK)){
                    LOGGER.info("释放计数器, 让当前请求来获得分布式锁...");
                    zkLocklatch.countDown();
                }
            }
        });
    }

    public void getLock(){
        while (true){
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK);
                LOGGER.info("获取分布式锁成功");
                return;
            } catch (Exception e) {
                LOGGER.info("获取分布式锁失败");
                try{
                    //如果没有获取锁 需要重新设置同步资源
                    if(zkLocklatch.getCount() <= 0){
                        zkLocklatch = new CountDownLatch(1);
                    }
                    //阻塞当前未获取锁的线程
                    zkLocklatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //释放锁
    public boolean releaseLock(){
        try {
            String lockPath = "/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK;
            if(client.checkExists().forPath(lockPath) != null){
                client.delete().forPath(lockPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        LOGGER.info("分布式锁释放成功");
        return true;
    }
}
