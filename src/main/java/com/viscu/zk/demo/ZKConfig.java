package com.viscu.zk.demo;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ zk参数配置文件
 */

public class ZKConfig {

    public static final String zkServerPathSingle = "127.0.0.1:2181";

    public static final String zkServerPathCluster = "127.0.0.1:2281,127.0.0.1:2282,127.0.0.1:2283";

    public static final Integer timeout = 5000;

}
