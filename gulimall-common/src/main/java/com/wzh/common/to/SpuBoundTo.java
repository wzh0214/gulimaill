package com.wzh.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wzh
 * @data 2022/11/6 -15:34
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
