package com.wzh.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzh.common.utils.PageUtils;
import com.wzh.gulimall.order.entity.MqMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 21:07:01
 */
public interface MqMessageService extends IService<MqMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

