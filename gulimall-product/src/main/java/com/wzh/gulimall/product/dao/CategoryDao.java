package com.wzh.gulimall.product.dao;

import com.wzh.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 18:05:35
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
