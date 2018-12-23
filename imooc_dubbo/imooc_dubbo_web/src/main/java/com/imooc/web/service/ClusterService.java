package com.imooc.web.service;

/**
 * @ Create by ostreamBaba on 18-12-23
 * @ 描述
 */
public interface ClusterService {

    /**
     * @Description: 购买商品
     */
    void doBuyItem(String itemId);

    boolean displayBuy(String itemId);
}

