package com.wzh.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wzh
 * @data 2022/11/28 -15:25
 */
@Data
public class FareVo {
    private MemberAddressVo address;

    private BigDecimal fare;
}
