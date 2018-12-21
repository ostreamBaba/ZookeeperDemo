package com.viscu.zk.demo;

import org.apache.zookeeper.AsyncCallback;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class DeleteCallBack implements AsyncCallback.VoidCallback {

    @Override
    public void processResult(int rc, String path, Object ctx) {
        System.out.println("删除节点" + path);
        System.out.println((String)ctx);
    }
}
