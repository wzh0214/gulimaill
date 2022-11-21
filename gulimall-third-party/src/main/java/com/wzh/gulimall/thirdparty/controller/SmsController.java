package com.wzh.gulimall.thirdparty.controller;

import com.wzh.common.utils.R;
import com.wzh.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author wzh
 * @data 2022/11/20 -14:46
 */
@RequestMapping("/sms")
@Controller
public class SmsController {
    @Autowired
    private SmsComponent smsComponent;

    /**
     * 提供给别的服务调用，发送验证码
     * @param phone
     * @param code
     * @return
     */
    @ResponseBody
    @PostMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        smsComponent.sendSmsCode(phone, code);
        return R.ok();

    }
}
