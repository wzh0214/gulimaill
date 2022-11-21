package com.wzh.gulimall.product.web;

import com.wzh.gulimall.product.service.SkuInfoService;
import com.wzh.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @author wzh
 * @data 2022/11/18 -19:06
 */
@Controller
public class ItemController {
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 展示当前sku的详情
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {

         SkuItemVo vo  = skuInfoService.item(skuId);
         model.addAttribute("item", vo);

        return "item";
    }
}
