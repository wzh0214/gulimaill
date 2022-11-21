package com.wzh.gulimall.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author wzh
 * @data 2022/11/20 -20:21
 */
@Data
public class UserRegistVo {

    private String userName;


    private String password;


    private String phone;
}
