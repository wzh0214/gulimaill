package com.wzh.gulimall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wzh
 * @data 2022/11/27 -18:35
 */
@Data
public class FareVo {
    private MemberAddressVo address;
    private BigDecimal fare;
}
