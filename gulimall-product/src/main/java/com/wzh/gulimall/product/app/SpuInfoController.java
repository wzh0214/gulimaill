package com.wzh.gulimall.product.app;

import java.util.Arrays;
import java.util.Map;


import com.wzh.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wzh.gulimall.product.entity.SpuInfoEntity;
import com.wzh.gulimall.product.service.SpuInfoService;
import com.wzh.common.utils.PageUtils;
import com.wzh.common.utils.R;



/**
 * spu信息
 *
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 18:05:35
 */
@RestController
@RequestMapping("product/spuinfo")
public class    SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @GetMapping(value = "/skuId/{skuId}")
    R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId) {
        SpuInfoEntity entity = spuInfoService.getSpuInfoBySkuId(skuId);
        return R.ok().setData(entity);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        //PageUtils page = spuInfoService.queryPage(params);
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 商品上架
     */
    // /product/spuinfo/{spuId}/up
    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId) {
        spuInfoService.up(spuId);
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuSaveVo vo){
		//spuInfoService.save(spuInfo);
        spuInfoService.saveSpuInfo(vo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
