package com.imooc.web.controller;

import com.imooc.common.utils.IMoocJSONResult;
import com.imooc.web.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Create by ostreamBaba on 18-12-25
 * @ 描述
 */


@RestController
public class TestController {

    @Autowired
    private ClusterService clusterService;

    @GetMapping("/buy1")
    public IMoocJSONResult buy(String itemId){
        boolean result = clusterService.displayBuy(itemId);
        return IMoocJSONResult.ok(result?"订单创建成功":"订单创建失败");
    }

    //模拟集群下的数据不一样
    @GetMapping("/buy2")
    public IMoocJSONResult buy2(String itemId){
        boolean result = clusterService.displayBuy(itemId);
        return IMoocJSONResult.ok(result?"订单创建成功":"订单创建失败");
    }

}
