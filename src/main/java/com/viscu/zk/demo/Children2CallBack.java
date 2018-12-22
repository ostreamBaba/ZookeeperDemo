package com.viscu.zk.demo;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 比ChildrenCallBack多了一个Stat参数 用于输出当前节点的状态信息
 */
public class Children2CallBack implements AsyncCallback.Children2Callback {

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        //打印子节点列表
        for (String s : children){
            System.out.println(s);
        }
        System.out.println("ChildrenCallBack: " + path);
        System.out.println((String)ctx);
        System.out.println(stat.toString());
    }
}
