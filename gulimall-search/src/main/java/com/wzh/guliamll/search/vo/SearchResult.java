package com.wzh.guliamll.search.vo;

import com.wzh.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author wzh
 * @data 2022/11/16 -14:20
 */
@Data
public class SearchResult {
    // 查询到的所有商品信息
    private List<SkuEsModel> product;
    /**
     * 分页信息
     */
    private Integer pageNum; // 当前页码
    private Long total; // 总记录数
    private Integer totalPages; // 总页码数
    private List<Integer> pageNavs; // 导航页

    private List<BrandVo> brands; // 当前查询到的结果，所有涉及到的品牌
    private List<CatalogVo> catalogs; // 当前查询到的结果，所有涉及到分类
    private List<AttrVo> attrs; // 当前查询到的结果，所有涉及到的所有属性

    //========以上是返还给页面的信息==========
    /* 面包屑导航数据 */
    private List<NavVo> navs;


    @Data
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }

    @Data
    public static class BrandVo {
        private Long  brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class CatalogVo{
        private Long  catalogId;
        private String catalogName;

    }


    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
