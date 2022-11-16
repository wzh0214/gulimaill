package com.wzh.guliamll.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzh
 * @data 2022/11/16 -13:55
 * catalog3Id=255&keyword=小米&sort=saleCount_asc&hasStock=0/1&brandId=1&brandId=2
 */
@Data
public class SearchParam {
    private String keyword; // 页面传递过来的全文匹配关机字
    private Long catalog3Id; // 三级分类id

    private String sort; // 排序条件

    // 过滤条件
    private Integer hasStock = 1; // 是否显示有货，默认null查所有
    private String skuPrice; // 价格区间查询
    private List<Long> brandId; // 安装品牌查询，可以多选
    private List<String> attrs; // 按照属性进行删选
    private Integer pageNum; // 页码

}
