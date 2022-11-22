package com.wzh.gulimall.auth.vo;

import lombok.Data;

/**
 * @author wzh
 * @data 2022/11/21 -18:18
 */
@Data
public class SocialUser {
    private String access_token;

    private String remind_in;

    private long expires_in;

    private String uid;

    private String isRealName;
}
