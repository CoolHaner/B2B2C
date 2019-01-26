package com.pinyougou.page.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

@Service
public class ItemPageServiceImpl implements ItemPageService {

	@Value("${pagedir}")
	private String pagedir;

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public boolean genItemHtml(Long goodsId) {
		Configuration configuration = freeMarkerConfig.getConfiguration();
		try {
			Template template = configuration.getTemplate("item.ftl");
			Map dataModel = new HashMap<>();
			// 商品主表数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);

			// 商品扩展表数据
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);

			// 读取商品分类
			String name1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String name2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String name3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat1", name1);
			dataModel.put("itemCat2", name2);
			dataModel.put("itemCat3", name3);

			// 读取SKU列表
			TbItemExample example = new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andGoodsIdEqualTo(goodsId);
			criteria.andStatusEqualTo("1");// 状态有效
			example.setOrderByClause("is_default desc");// 按是否是默认字段进行降序排序，目的是返回第一条结果为默认SKU
			List<TbItem> itemList = itemMapper.selectByExample(example);
			dataModel.put("itemList", itemList);

			Writer out = new FileWriter(pagedir + goodsId + ".html");
			template.process(dataModel, out);// 输出
			out.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteHtml(Long[] goodsIds) {
		try {
			for (Long goodsId : goodsIds) {
				new File(pagedir + goodsId + ".html").delete();
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}
