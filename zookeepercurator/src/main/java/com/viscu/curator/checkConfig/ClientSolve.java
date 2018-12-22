package com.viscu.curator.checkConfig;

import com.viscu.curator.utils.JsonUtils;
import com.viscu.curator.utils.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 对配置文件进行处理
 */
public class ClientSolve {

    public static void solve(PathChildrenCache childrenCache, String basePath, String subPath) throws InterruptedException {
        childrenCache.getListenable().addListener((client, event)->{
            // 监听节点变化
            System.out.println(basePath+subPath);
            if(event.getType().equals( PathChildrenCacheEvent.Type.CHILD_UPDATED)){
                String configNodePath = event.getData().getPath();
                if (configNodePath.equals(basePath + subPath)) {
                    System.out.println("监听到配置发生变化，节点路径为:" + configNodePath);

                    // 读取节点数据
                    String jsonConfig = new String(event.getData().getData());
                    System.out.println("节点" + basePath + "的数据为: " + jsonConfig);

                    // 从json转换配置
                    RedisConfig redisConfig = null;
                    if (StringUtils.isNotBlank(jsonConfig)) {
                        redisConfig = JsonUtils.jsonToPojo(jsonConfig, RedisConfig.class);
                    }

                    // 配置不为空则进行相应操作
                    if (redisConfig != null) {
                        String type = redisConfig.getType();
                        String url = redisConfig.getUrl();
                        String remark = redisConfig.getRemark();
                        // 判断事件
                        if (type.equals("add")) {
                            System.out.println("监听到新增的配置，准备下载...");
                            // ... 连接ftp服务器，根据url找到相应的配置
                            Thread.sleep(500);
                            System.out.println("开始下载新的配置文件，下载路径为<" + url + ">");
                            // ... 下载配置到你指定的目录
                            Thread.sleep(1000);
                            System.out.println("下载成功，已经添加到项目中");
                            // ... 拷贝文件到项目目录
                        } else if (type.equals("update")) {
                            System.out.println("监听到更新的配置，准备下载...");
                            // ... 连接ftp服务器，根据url找到相应的配置
                            Thread.sleep(500);
                            System.out.println("开始下载配置文件，下载路径为<" + url + ">");
                            // ... 下载配置到你指定的目录
                            Thread.sleep(1000);
                            System.out.println("下载成功...");
                            System.out.println("删除项目中原配置文件...");
                            Thread.sleep(100);
                            // ... 删除原文件
                            System.out.println("拷贝配置文件到项目目录...");
                            // ... 拷贝文件到项目目录
                        } else if (type.equals("delete")) {
                            System.out.println("监听到需要删除配置");
                            System.out.println("删除项目中原配置文件...");
                        }

                        // TODO 视情况统一重启服务
                    }
                }
            }
        });
    }

}
