package com.wzh.guliamll.search.controller;

import com.wzh.common.exception.BizCodeEnum;
import com.wzh.common.to.es.SkuEsModel;
import com.wzh.common.utils.R;
import com.wzh.guliamll.search.service.ProductService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/12 -15:37
 */
@Slf4j
@RequestMapping("search/save")
@RestController
public class EsController {

    @Autowired
    private ProductService productService;

    /**
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUP(@RequestBody List<SkuEsModel> skuEsModes) {
        boolean status=false;
        try {
            status = productService.productStatusUP(skuEsModes);
        } catch (IOException e) {
            log.error("ESController商品上架错误{}",e);

            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if(status){
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }else {
            return R.ok();
        }
    }

}
