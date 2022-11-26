package com.wzh.common.to;

import lombok.Data;

/**
 * @author wzh
 * @data 2022/11/12 -13:48
 */
@Data
public class SkuHasStockTo {
    private Long skuId;

    private Boolean hasStock;
}
