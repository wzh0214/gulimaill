package com.wzh.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wzh.common.constant.AuthServerConstant;
import com.wzh.common.utils.HttpUtils;
import com.wzh.common.utils.R;
import com.wzh.gulimall.auth.feign.MemberFeignService;
import com.wzh.common.vo.MemberRespVo;
import com.wzh.gulimall.auth.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wzh
 * @data 2022/11/21 -17:07
 * 处理社交登录请求
 */
@Controller
public class OAuth2Controller {
    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();

        HashMap<String, String> map = new HashMap<>();
        map.put("client_id", "2636917288");
        map.put("client_secret", "6a263e9284c6c1a74a62eadacc11b6e2");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://gulimall.com/oauth2.0/weibo/success");
        map.put("code", code);


        // 1.根据code换取accessToken
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", headers, querys, map);

        // 2. 处理
        if (response.getStatusLine().getStatusCode() == 200) {
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            // 1.当前用户如果是第一次登录，自动注册；注册过就登录
            R r = memberFeignService.oauthlogin(socialUser);
            if (r.getCode() == 0) {
                MemberRespVo data = r.getData(new TypeReference<MemberRespVo>() {});
                session.setAttribute(AuthServerConstant.LOGIN_USER, data);

                // 2.登录成功转到首页
                return "redirect:http://gulimall.com";
            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }

        } else {
            return "redirect:http://auth.gulimall.com/login.html";
        }



    }
}
