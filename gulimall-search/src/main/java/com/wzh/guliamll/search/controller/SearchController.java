package com.wzh.guliamll.search.controller;

import com.wzh.guliamll.search.service.MallSearchService;
import com.wzh.guliamll.search.vo.SearchParam;
import com.wzh.guliamll.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wzh
 * @data 2022/11/15 -21:23
 */
@Controller
public class SearchController {

    @Autowired
    private MallSearchService mallSearchService;

    /**
     * springmvc会自动将所有请求参数封装为指定对象
     * @param param
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model) {
        // 1.根据页面传递来的查询参数，去es中检索商品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);

        return "list";

    }
}