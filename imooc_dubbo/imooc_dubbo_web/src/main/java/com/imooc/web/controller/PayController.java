package com.imooc.web.controller;

import com.imooc.web.service.ClusterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.common.utils.IMoocJSONResult;

/**
 * @Description: 订购商品controller
 */

@Controller
public class PayController {

    @Autowired
    private ClusterService buyService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/buy")
    @ResponseBody
    public IMoocJSONResult doGetlogin(String itemId) {

        if (StringUtils.isNotBlank(itemId)) {
            buyService.displayBuy(itemId);
        } else {
            return IMoocJSONResult.errorMsg("商品id不能为空");
        }

        return IMoocJSONResult.ok();
    }


    @GetMapping("/test/{itemId}")
    @ResponseBody
    public boolean test(@PathVariable String itemId){
        buyService.doBuyItem(itemId);
        return true;
    }

}