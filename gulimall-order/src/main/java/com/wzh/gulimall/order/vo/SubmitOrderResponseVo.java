package com.wzh.gulimall.order.vo;

import com.wzh.gulimall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author wzh
 * @data 2022/11/28 -14:00
 */
@Data
public class SubmitOrderResponseVo {
    private OrderEntity order;

    /** 错误状态码  0代表成功 1代表失败 2价格校验错误 3库存锁定失败**/
    private Integer code;

}
