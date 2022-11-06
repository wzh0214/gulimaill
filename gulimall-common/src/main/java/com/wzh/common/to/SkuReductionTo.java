package com.wzh.common.to;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/6 -16:03
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<MemberPriceVo> memberPrice;

    @NoArgsConstructor
    @Data
    public static class MemberPriceVo {
        private Long id;
        private String name;
        private BigDecimal price;
    }
}
