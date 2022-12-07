package com.wzh.gulimall.ware.vo;

import lombok.Data;

/**
 * @author wzh
 * @data 2022/11/29 -12:19
 */
@Data
public class LockStockResult {
    private Long skuId;

    private Integer num;

    private Boolean locked;
}
