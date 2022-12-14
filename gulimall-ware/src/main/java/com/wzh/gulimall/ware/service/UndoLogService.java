package com.wzh.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzh.common.utils.PageUtils;
import com.wzh.gulimall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 21:12:17
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

