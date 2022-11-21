package com.wzh.gulimall.member.exception;

/**
 * @author wzh
 * @data 2022/11/20 -20:40
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("手机号已被注册");
    }
}
