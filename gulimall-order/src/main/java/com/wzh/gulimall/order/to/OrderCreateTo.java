package com.wzh.gulimall.order.to;

import com.wzh.gulimall.order.entity.OrderEntity;
import com.wzh.gulimall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/28 -14:56
 */
@Data
public class OrderCreateTo {
    //订单
    private OrderEntity order;

    // 订单项
    private List<OrderItemEntity> orderItems;

    /** 订单计算的应付价格 **/
    private BigDecimal payPrice;

    /** 运费 **/
    private BigDecimal fare;

}
