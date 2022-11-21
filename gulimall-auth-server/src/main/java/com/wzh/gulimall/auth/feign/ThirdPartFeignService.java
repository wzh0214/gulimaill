package com.wzh.gulimall.auth.feign;

import com.wzh.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wzh
 * @data 2022/11/20 -14:50
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartFeignService {
    @PostMapping("/sms/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
