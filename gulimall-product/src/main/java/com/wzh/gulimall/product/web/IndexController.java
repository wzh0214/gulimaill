package com.wzh.gulimall.product.web;

import com.wzh.gulimall.product.entity.CategoryEntity;
import com.wzh.gulimall.product.service.CategoryService;
import com.wzh.gulimall.product.vo.Catelog2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wzh
 * @data 2022/11/12 -20:09
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        // 1.查出所有的一级分类
        List<CategoryEntity> categoryEntityList =  categoryService.getLevel1Categorys();
       model.addAttribute("categorys", categoryEntityList);
       return "index";
    }


    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2VO>> getCatelogJson(){
        Map<String, List<Catelog2VO>>  map = categoryService.getCatalogJson();

        return map;
    }



}
