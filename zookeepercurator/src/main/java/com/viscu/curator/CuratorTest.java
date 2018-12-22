package com.viscu.curator;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 测试类
 */
public class CuratorTest {

    String nodePath = "/super/node";

    CuratorOperator cto = new CuratorOperator();

    @Test
    public void testCreate() throws Exception {
        //创建节点 递归创建节点
        String nodePath = "/super/node/1/2/3";
        byte[] data = "supreme".getBytes();
        cto.client.create().creatingParentsIfNeeded() //加上可以递归创建节点
                .withMode( CreateMode.PERSISTENT)
                .withACL( ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(nodePath, data);
    }

    @Test
    public void testSetData() throws Exception {
        byte[] newDate = "batman".getBytes();
        cto.client.setData()
                .withVersion(0) //乐观锁
                .forPath(nodePath, newDate);
    }

    @Test
    public void testDel() throws Exception {
        cto.client.delete()
                .guaranteed() //如果删除失败 那么在后端还是会继续删除 直到成功 (当发生网络抖动 保证该操作一定执行)
                .deletingChildrenIfNeeded() //如果有子节点 就删除
                .withVersion(0)
                .forPath(nodePath);
    }


    @Test
    public void testGetData() throws Exception {// 读取节点数据
		Stat stat = new Stat();
		byte[] data = cto.client.getData()
                .storingStatIn(stat)
                .forPath(nodePath);
		System.out.println("节点" + nodePath + "的数据为: " + new String(data));
		System.out.println("该节点的版本号为: " + stat.getVersion());
    }

    @Test
    public void testGetChildList() throws Exception {
        List<String> childNodes = cto.client.getChildren()
											.forPath(nodePath);
		System.out.println("开始打印子节点：");
		for (String s : childNodes) {
			System.out.println(s);
		}
    }

    @Test
    public void testNodeExist() throws Exception {
        // 判断节点是否存在,如果不存在则为空
		Stat statExist = cto.client.checkExists().forPath(nodePath + "/1");
		System.out.println(statExist);
    }

    @Test
    public void testWatch() throws Exception {
        //watcher事件 当使用usingWatcher的时候，监听只会触发一次，监听完毕后就销毁
		cto.client.getData().usingWatcher(new MyCuratorWatcher()).forPath(nodePath); //curator
		//cto.client.getData().usingWatcher(new MyWatcher()).forPath(nodePath); //原生watcher
        Thread.sleep(100000);
    }

    @Test
    public void testNodeCache() throws Exception {
        //利用NodeCache实现 注册一次 N次监听

        //NodeCache: 监听节点数据的变更 会触发事件
        final NodeCache nodeCache = new NodeCache(cto.client, nodePath);
        //buildInitial: 初始化的时候将获取node的值并且缓存
        nodeCache.start(true);
        if(nodeCache.getCurrentData() != null){
            System.out.println("节点初始化的数据为: " + new String(nodeCache.getCurrentData().getData()));
        }else {
            System.out.println("节点初始化数据为空...");
        }
        //监听nodePath节点的数据
        nodeCache.getListenable().addListener(()->{
            //若监听节点被删除会抛出空指针异常
            String data = new String(nodeCache.getCurrentData().getData());
            System.out.println("节点路径： " + nodeCache.getCurrentData().getPath() + "数据： " + data);
        });
        Thread.sleep(1000000);
    }

    @Test
    public void testChildNodePathCache() throws Exception {
        //为子节点添加watcher
        String childNodePathCache = nodePath;
        //true会把状态缓存到Stat中
        final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, childNodePathCache, true);
        /**
         * StartMode: 初始化方式
         * NORMAL: 异步初始化 不会触发事件
         * BUILD_INITIAL_CACHE: 同步初始化
         * POST_INITIALIZED_EVENT: 异步初始化 初始化后会触发事件
         */
        childrenCache.start( PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        List<ChildData> childDataList = childrenCache.getCurrentData();
        System.out.println("当前数据节点的子节点数据列表: ");
        for (ChildData cd : childDataList){
            String childData = new String(cd.getData());
            System.out.println(childData);
        }

        childrenCache.getListenable().addListener((client, event)->{
            if(event.getType().equals( PathChildrenCacheEvent.Type.INITIALIZED)){
                System.out.println("节点初始化ok...");
            }else if(event.getType().equals( PathChildrenCacheEvent.Type.CHILD_ADDED)){
                String path = event.getData().getPath();
                if(path.equals(ADD_PATH )){
                    System.out.println("添加子节点：" + path);
                    System.out.println("子节点数据： " + new String(event.getData().getData()));
                }else{
                    System.out.println("添加不正确...");
                }

            }else if(event.getType().equals( PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                System.out.println("删除子节点： " + event.getData().getPath());
            }else if(event.getType().equals( PathChildrenCacheEvent.Type.CHILD_UPDATED)){
                System.out.println("修改子节点路径： " + event.getData().getPath());
                System.out.println("修改子节点数据： " + new String(event.getData().getData()));
            }
        });

        Thread.sleep(10000000);
    }


    public static final String ADD_PATH = "/super/node/d";

}
