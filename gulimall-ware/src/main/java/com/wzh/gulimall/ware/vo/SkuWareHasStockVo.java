package com.wzh.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzh
 * @data 2022/11/29 -16:18
 */
@Data
public class SkuWareHasStockVo {
    private Long skuId;
    private Integer num;
    private List<Long> wareId;
}
