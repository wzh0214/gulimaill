package com.wzh.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.wzh.gulimall.product.entity.AttrEntity;
import com.wzh.gulimall.product.service.AttrAttrgroupRelationService;
import com.wzh.gulimall.product.service.AttrService;
import com.wzh.gulimall.product.service.CategoryService;
import com.wzh.gulimall.product.service.impl.CategoryServiceImpl;
import com.wzh.gulimall.product.vo.AttrGroupRelationVo;
import com.wzh.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wzh.gulimall.product.entity.AttrGroupEntity;
import com.wzh.gulimall.product.service.AttrGroupService;
import com.wzh.common.utils.PageUtils;
import com.wzh.common.utils.R;



/**
 * 属性分组
 *
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 18:05:36
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private  AttrAttrgroupRelationService relationService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        //根据id查询完整路径。      查询结果要添加分类路径字段，样式[2,34,225]
        Long[] path = categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 根据分组id查关联的所有基本属性
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> attrEntities = attrService.getRelationAttr(attrgroupId);

        return R.ok().put("data", attrEntities);
    }

    /**
     * 查询可以新建关联的属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                          @RequestParam Map<String, Object> params) { // 前端会传分页的信息
       PageUtils page =  attrService.getNoRelationAttr(attrgroupId, params); // 因为要分页

        return R.ok().put("page", page);
    }

    /**
     * 获取分类下所有分组&关联属性
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        // 1.查出当前分类下的所有属性分组
        // 2.查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrsByCatrlogId(catelogId);
        return R.ok().put("data", vos);
    }


    /**
     * 添加关联关系
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos) {
        relationService.saveBatch(vos);

        return R.ok();
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 删除关联关系
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos) {  // 因为可以批量删除，要用数组，也可以为list<AttrGroupRelationVo>
        attrService.deleteRelation(vos);

        return R.ok();
    }

}
