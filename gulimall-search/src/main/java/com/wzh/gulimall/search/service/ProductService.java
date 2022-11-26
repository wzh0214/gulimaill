package com.wzh.gulimall.search.service;

import com.wzh.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/12 -15:40
 */
public interface ProductService {
    boolean productStatusUP(List<SkuEsModel> skuEsModes) throws IOException;
}
