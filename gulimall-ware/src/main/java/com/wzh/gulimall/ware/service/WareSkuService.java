package com.wzh.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzh.common.to.mq.OrderTo;
import com.wzh.common.to.mq.StockLockedTo;
import com.wzh.common.utils.PageUtils;
import com.wzh.gulimall.ware.entity.WareSkuEntity;
import com.wzh.gulimall.ware.vo.LockStockResult;
import com.wzh.gulimall.ware.vo.SkuHasStockVo;
import com.wzh.gulimall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * εεεΊε­
 *
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 21:12:17
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);


    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo to);
}

