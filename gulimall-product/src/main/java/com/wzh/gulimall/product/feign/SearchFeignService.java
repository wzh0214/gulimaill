package com.wzh.gulimall.product.feign;

import com.wzh.common.to.es.SkuEsModel;
import com.wzh.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wzh
 * @data 2022/11/12 -16:12
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUP(@RequestBody List<SkuEsModel> skuEsModes);
}
