package com.wzh.gulimall.product.vo;

import lombok.Data;

/**
 * @author wzh
 * @data 2022/11/3 -19:47
 */
@Data
public class AttrRespVo extends AttrVo{
    /**
     * "catelogName": "手机/数码/手机", //所属分类名字
     * 	"groupName": "主体", //所属分组名字
     */

    private String catelogName;
    private String groupName;

    private Long[] catelogPath;
}
