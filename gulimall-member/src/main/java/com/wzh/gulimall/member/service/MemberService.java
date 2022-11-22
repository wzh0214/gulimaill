package com.wzh.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzh.common.utils.PageUtils;
import com.wzh.gulimall.member.entity.MemberEntity;
import com.wzh.gulimall.member.exception.PhoneExistException;
import com.wzh.gulimall.member.exception.UsernameExistException;
import com.wzh.gulimall.member.vo.MemberLoginVo;
import com.wzh.gulimall.member.vo.SocialUser;
import com.wzh.gulimall.member.vo.UserRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author wzh
 * @email wzh@gmail.com
 * @date 2022-10-25 20:56:20
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(UserRegistVo vo);

    void checkUserNameUnique(String userName) throws UsernameExistException;

    void checkPhoneUnique(String phone) throws PhoneExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser socialUser) throws Exception;
}

