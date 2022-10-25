package com.wzh.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzh.common.utils.PageUtils;
import com.wzh.gulimall.member.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 20:56:20
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

