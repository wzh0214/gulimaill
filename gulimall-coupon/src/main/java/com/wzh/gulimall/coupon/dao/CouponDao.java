package com.wzh.gulimall.coupon.dao;

import com.wzh.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 20:38:46
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
