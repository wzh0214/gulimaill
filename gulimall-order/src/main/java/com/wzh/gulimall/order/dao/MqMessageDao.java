package com.wzh.gulimall.order.dao;

import com.wzh.gulimall.order.entity.MqMessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 21:07:01
 */
@Mapper
public interface MqMessageDao extends BaseMapper<MqMessageEntity> {
	
}
