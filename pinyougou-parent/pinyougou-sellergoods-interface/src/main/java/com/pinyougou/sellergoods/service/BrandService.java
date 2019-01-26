package com.pinyougou.sellergoods.service;
/**
 * 品牌接口
 * @author Lenovo
 *
 */

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

public interface BrandService {

	public List<TbBrand> findAll();
	
	/**
	 * 产品分页
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	public void add(TbBrand brand);
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	 * 保存更新
	 * @param brand
	 */
	public void update(TbBrand brand);
	
	public void delete(Long[] ids);
	
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize);
	
	public List<Map> selectOptionList();
}
