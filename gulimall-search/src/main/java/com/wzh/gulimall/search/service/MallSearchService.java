package com.wzh.gulimall.search.service;

import com.wzh.gulimall.search.vo.SearchParam;
import com.wzh.gulimall.search.vo.SearchResult;

/**
 * @author wzh
 * @data 2022/11/16 -13:57
 */
public interface MallSearchService {
    /**
     *
     * @param param 检索的所有参数
     * @return 返回检索的结果，里面包含页面需要的所有信息
     */
    SearchResult search(SearchParam param);
}
