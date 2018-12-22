package com.viscu.curator;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class MyWatcher implements Watcher{
    @Override
    public void process(WatchedEvent event) {
        System.out.println("触发watcher, 节点路径： " + event.getPath());
    }
}
