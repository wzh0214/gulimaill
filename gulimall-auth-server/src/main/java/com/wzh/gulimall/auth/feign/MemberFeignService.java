package com.wzh.gulimall.auth.feign;

import com.wzh.common.utils.R;
import com.wzh.gulimall.auth.vo.SocialUser;
import com.wzh.gulimall.auth.vo.UserLoginVo;
import com.wzh.gulimall.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wzh
 * @data 2022/11/20 -21:40
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {
    // 因为调用远程服务传来的是json，所以用@RequestBody
    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);


    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);


    @PostMapping("/member/member/oauth2/login")
    R oauthlogin(@RequestBody SocialUser SocialUser) throws Exception;
}
