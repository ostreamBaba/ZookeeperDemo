package com.imooc.item.service.impl;

import com.imooc.item.mapper.ItemsMapper;
import com.imooc.item.pojo.Items;
import com.imooc.item.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("itemsService")
public class ItemsServiceImpl implements ItemsService {
	

	@Autowired(required = false)
	private ItemsMapper itemsMapper;


	@Override
	public Items getItem(String itemId) {
		return null;
	}

	@Override
	public int getItemCounts(String itemId) {
		Items item = itemsMapper.selectByPrimaryKey(itemId);
		return item.getCounts();
	}
	@Override
	public void displayReduceCounts(String itemId, int buyCounts) {
//		int a  = 1 / 0;
		Items reduceItem = new Items();
		reduceItem.setId(itemId);
		reduceItem.setBuyCounts(buyCounts);
		itemsMapper.reduceCounts(reduceItem);
	}

}

