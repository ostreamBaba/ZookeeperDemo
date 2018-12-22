package com.viscu.zk.demo;

import org.apache.zookeeper.AsyncCallback;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */

public class ChildrenCallBack implements AsyncCallback.ChildrenCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        //打印子节点列表
        for (String s : children){
            System.out.println(s);
        }
        System.out.println("ChildrenCallBack: " + path);
        System.out.println((String)ctx);
    }
}
