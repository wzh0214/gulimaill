package com.wzh.gulimall.ware.dao;

import com.wzh.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 21:12:17
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}