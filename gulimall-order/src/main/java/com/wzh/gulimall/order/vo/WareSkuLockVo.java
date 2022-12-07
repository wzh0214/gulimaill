package com.wzh.gulimall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzh
 * @data 2022/11/29 -12:04
 */
@Data
public class WareSkuLockVo {
    private String orderSn; // 订单号

    private List<OrderItemVo> locks; // 需要锁住的所有库存信息
}
