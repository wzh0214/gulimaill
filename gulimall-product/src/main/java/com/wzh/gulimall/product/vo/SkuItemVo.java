package com.wzh.gulimall.product.vo;

import com.wzh.gulimall.product.entity.SkuImagesEntity;
import com.wzh.gulimall.product.entity.SkuInfoEntity;
import com.wzh.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.PrimitiveIterator;

/**
 * @author wzh
 * @data 2022/11/18 -19:53
 */
@Data
public class SkuItemVo {
    // 1.sku基本信息获取 pms_sku_info
    private SkuInfoEntity info;

    boolean hasStock = true;

    // 2.sku图片信息 pms_sku_images
    private List<SkuImagesEntity> images;
    // 3.获取spu的销售属性
    List<SkuItemSaleAttrVo> saleAttr;
    // 4.获取spu的介绍 pms_spu_info_desc
    private SpuInfoDescEntity desp;
    // 5.获取spu的规格参数信息
    private List<SpuItemAttrGroupVo>  groupAttrs;


    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;
        private String attrName;
        private List<AttrValueWithSkuIdVo> attrValues;
    }

    @Data
    public static class AttrValueWithSkuIdVo {
        private String attrValue;
        private String skuIds;
    }



    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<SpuBaseAttrVo> attrs;

    }

    @Data
    public static class SpuBaseAttrVo {
        private String attrName;
        private String attrValue;
    }


}
