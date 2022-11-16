package com.wzh.guliamll.search.service.impl;

import com.wzh.guliamll.search.config.GulimallElasticsearshConfig;
import com.wzh.guliamll.search.constant.EsConstant;
import com.wzh.guliamll.search.service.MallSearchService;
import com.wzh.guliamll.search.vo.SearchParam;
import com.wzh.guliamll.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.Highlighter;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/16 -13:58
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;
    private Highlighter build;


    // 去es进行检索
    @Override
    public SearchResult search(SearchParam param) {
        // 1.动态构建出查询需要的DSL语句
        SearchResult result = null;

        // 1.准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);

        try {
            // 2.执行检索请求
            SearchResponse response = client.search(searchRequest, GulimallElasticsearshConfig.COMMON_OPTIONS);

            // 3.分析响应数据封装我们需要的格式
            result = buildSearchResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 准备检索请求
     * 模糊匹配keyword，过滤(分类id。品牌id，价格区间，是否有库存，规格属性)，排序，分页，高亮，聚合分析
     * GET /gulimall-product/_search
     * {
     *   "query": {
     *     "bool": {
     *       "must": [
     *         {}
     *       ],
     *       "filter": [
     *         {},
     *         {},
     *         {},
     *         {},
     *         {}
     *       ]
     *     }
     *   },
     *   "sort": [
     *     {}
     *   ],
     *   "from": 0,
     *   "size": 2,
     *   "highlight": {}
     *   "aggs": {}
     * }
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); // 构建DSl语句
//
//
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//
//        // 1.模糊匹配keyword
//        if (!StringUtils.isEmpty(param.getKeyword())) {
//            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
//        }
//
//        // 2.过滤
//        // 2.1 分类id
//        Long catalog3Id = param.getCatalog3Id();
//        if (catalog3Id != null) {
//            boolQuery.filter(QueryBuilders.termQuery("catalogId", catalog3Id));
//        }
//
//        // 2.2 品牌id
//        List<Long> brandId = param.getBrandId();
//        if (!CollectionUtils.isEmpty(brandId)) {
//            boolQuery.filter(QueryBuilders.termQuery("brandId", brandId));
//        }
//
//        // 2.3 价格区间 1_500 / _500 / 500_  看传递的参数，如果是_500表示查小于500的
//
//        String price = param.getSkuPrice();
//        if (!StringUtils.isEmpty(price)) {
//            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
//            String[] s = price.split("_"); // 1_500 / _500 分割后长度都为2，_500 s[0]=null, s[1]=500
//            if (s.length == 2) {
//                rangeQuery.gte(s[0]).lte(s[1]);
//            } else {
//                rangeQuery.gte(s[0]);
//            }
//            boolQuery.filter(rangeQuery);
//        }
//
//        // 2.4 库存
//        if (param.getHasStock() != null) {
//            boolean flag = param.getHasStock() == 0 ? false : true;
//            boolQuery.filter(QueryBuilders.termQuery("hasStock", flag));
//        }
//
//
//        // 2.5 规格属性
//
//        if (!CollectionUtils.isEmpty(param.getAttrs())) {
//            for (String attr : param.getAttrs()) {
//                // attrs=1_5寸:8寸&attrs=2_16G:8G
//                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
//                String[] s = attr.split("_");
//                String attrId = s[0]; // 检索的属性id
//                String[] attrValues = s[1].split(":");
//                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
//                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attValue", attrValues));
//                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
//                boolQuery.filter(nestedQuery);
//
//            }
//        }
//        // 第一部分bool查询组合结束
//        sourceBuilder.query(boolQuery);
//
//        // 3.排序
//        if (!StringUtils.isEmpty(param.getSort())) {
//            String sort = param.getSort();
//            //sort=hotScore_asc/dec
//            String[] s = sort.split("_");
//            SortOrder order = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
//            sourceBuilder.sort(s[0], order);
//        }
//
//        // 4.分页
//        sourceBuilder.from(param.getPageNum() == null ? 0 :(param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
//        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
//
//        // 5.高亮
//        if (!StringUtils.isEmpty(param.getKeyword())) { // 带有查询条件才高亮显示
//            HighlightBuilder builder = new HighlightBuilder();
//            builder.field("skuTitle");
//            builder.preTags("<b style='color:red'>");
//            builder.postTags("</b>");
//            sourceBuilder.highlighter(builder);
//        }
//
//        System.out.println(sourceBuilder.toString());
//
//        // 聚合分析
//
//        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
//        return searchRequest;


        SearchRequest searchRequest = new SearchRequest();

        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 构建bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 1.模糊匹配keyword
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        // 2.过滤(分类id。品牌id，价格区间，是否有库存，规格属性)，
        // 2.1 分类id
        if (param.getCatalog3Id() != null &&  param.getCatalog3Id() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catelogId", param.getCatalog3Id()));
        }
        // 2.2 品牌id
        List<Long> brandId = param.getBrandId();
        if (!CollectionUtils.isEmpty(brandId)) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", brandId));
        }
        // 2.3 价格区间 1_500 / _500 / 500_
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
        String price = param.getSkuPrice();
        if (!StringUtils.isEmpty(price)) {
            String[] priceInfo = price.split("_");
            // 1_500
            if (priceInfo.length == 2) {
                rangeQueryBuilder.gte(priceInfo[0]).lte(priceInfo[1]);
                //    _500
            } else if (price.startsWith("_")) {
                rangeQueryBuilder.lte(priceInfo[0]);
                //    500_
            } else {
                rangeQueryBuilder.gte(priceInfo[0]);
            }
        }
        boolQueryBuilder.filter(rangeQueryBuilder);
        // 2.4 库存
        if (param.getHasStock() != null) {
            boolean flag = param.getHasStock() == 0 ? false : true;
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", flag));
        }
        // 2.5 规格属性
        // attrs=1_钢精:铝合&attrs=2_anzhuo:apple&attrs=3_lisi ==> attrs=[1_钢精:铝合,2_anzhuo:apple,3_lisi]
        List<String> attrs = param.getAttrs();
        if (!CollectionUtils.isEmpty(attrs)) {
            // 每个属性参数 attrs=1_钢精:铝合 ==》 nestedQueryFilter
            /**
             *          {
             *           "nested": {
             *             "path": "",
             *             "query": {
             *               "bool": {
             *                 "must": [
             *                   {},
             *                   {}
             *                 ]
             *               }
             *             }
             *           }
             *         },
             */
            for (String attr : attrs) {
                String[] attrInfo = attr.split("_");
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                boolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrInfo[0]));
                boolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrInfo[1].split(":")));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            }
        }

        // 第一部分bool查询组合结束
        builder.query(boolQueryBuilder);

        // 3.排序，sort=hotScore_asc/desc
        String sortStr = param.getSort();
        if (!StringUtils.isEmpty(sortStr)) {
            String[] sortInfo = sortStr.split("_");
            SortOrder sortType = sortInfo[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            builder.sort(sortInfo[0], sortType);
        }

        // 4.分页，
        builder.from(param.getPageNum() == null ? 0 : (param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        builder.size(EsConstant.PRODUCT_PAGESIZE);
        // 5.高亮，查询关键字不为空才有结果高亮
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle").preTags("<b style='color:red'>").postTags("</b>");
            builder.highlighter(highlightBuilder);
        }

        System.out.println(builder.toString());

        // 聚合分析

        SearchRequest search = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, builder);
        return search;


    }


    /**
     * 构建结果数据
     * @param response
     */
    private SearchResult buildSearchResult(SearchResponse response) {
        return null;
    }


}
