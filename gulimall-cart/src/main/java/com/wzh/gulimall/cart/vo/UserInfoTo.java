package com.wzh.gulimall.cart.vo;

import lombok.Data;

/**
 * @author wzh
 * @data 2022/11/23 -16:34
 *
 */
@Data
public class UserInfoTo {
    private Long userId; // 登录用户的id
    private String userKey; // 临时用户的值，就是登录了也会设置

    private boolean tempUser = false;
}
