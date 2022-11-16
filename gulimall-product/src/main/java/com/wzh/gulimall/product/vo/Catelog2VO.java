package com.wzh.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wzh
 * @data 2022/11/12 -20:49
 *
 * 二级分类id
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2VO {
    /**
     * 一级父分类的id
     */
    private String catalog1Id;

    /**
     * 三级子分类
     */
    private List<Category3Vo> catalog3List;

    private String id;

    private String name;


    /**
     * 三级分类vo
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category3Vo {

        /**
         * 二级分类id
         */
        private String catalog2Id;

        private String id;

        private String name;
    }
}
