package com.wzh.gulimall.product.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/5 -19:12
 */
@NoArgsConstructor
@Data
public class SpuSaveVo {
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private Integer publishStatus;
    private List<String> decript;
    private List<String> images;
    private BoundsVo bounds;
    private List<BaseAttrsVo> baseAttrs;
    private List<SkusVo> skus;

    @NoArgsConstructor
    @Data
    public static class BoundsVo {
        private Integer buyBounds;
        private Integer growBounds;
    }

    @NoArgsConstructor
    @Data
    public static class BaseAttrsVo {
        private Long attrId;
        private String attrValues;
        private Integer showDesc;
    }

    @NoArgsConstructor
    @Data
    public static class SkusVo {
        private List<AttrVo> attr;
        private String skuName;
        private BigDecimal price;
        private String skuTitle;
        private String skuSubtitle;
        private List<ImagesVo> images;
        private List<String> descar;
        private Integer fullCount;
        private BigDecimal discount;
        private Integer countStatus;
        private BigDecimal fullPrice;
        private BigDecimal reducePrice;
        private Integer priceStatus;
        private List<MemberPriceVo> memberPrice;

        @NoArgsConstructor
        @Data
        public static class AttrVo {
            private Long attrId;
            private String attrName;
            private String attrValue;
        }

        @NoArgsConstructor
        @Data
        public static class ImagesVo {
            private String imgUrl;
            private Integer defaultImg;
        }

        @NoArgsConstructor
        @Data
        public static class MemberPriceVo {
            private Long id;
            private String name;
            private BigDecimal price;
        }
    }
}
