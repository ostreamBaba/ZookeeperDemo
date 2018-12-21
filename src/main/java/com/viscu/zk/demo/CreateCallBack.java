package com.viscu.zk.demo;

import org.apache.zookeeper.AsyncCallback;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 异步创建节点 回调类
 */

public class CreateCallBack implements AsyncCallback.StringCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("创建节点： " + path);
        System.out.println((String) ctx);
    }
}
