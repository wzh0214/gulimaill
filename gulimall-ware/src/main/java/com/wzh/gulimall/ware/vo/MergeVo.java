package com.wzh.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wzh
 * @data 2022/11/7 -15:58
 */
@Data
public class MergeVo {
    private Long purchaseId; // 要写成包装类，如果没传值可以为null
    private List<Long> items; // 合并项集合
}
