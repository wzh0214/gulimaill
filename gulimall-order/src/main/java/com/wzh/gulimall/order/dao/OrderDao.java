package com.wzh.gulimall.order.dao;

import com.wzh.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 21:07:01
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
