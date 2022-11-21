package com.wzh.gulimall.member.exception;

/**
 * @author wzh
 * @data 2022/11/20 -20:39
 */
public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("用户名已存在");
    }
}
