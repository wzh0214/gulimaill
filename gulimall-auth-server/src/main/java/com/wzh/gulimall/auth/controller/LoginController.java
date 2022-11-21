package com.wzh.gulimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.client.config.impl.CacheData;
import com.wzh.common.constant.AuthServerConstant;
import com.wzh.common.exception.BizCodeEnum;
import com.wzh.common.utils.R;
import com.wzh.gulimall.auth.feign.MemberFeignService;
import com.wzh.gulimall.auth.feign.ThirdPartFeignService;
import com.wzh.gulimall.auth.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.plugin2.util.SystemUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wzh
 * @data 2022/11/19 -19:22
 */
@Controller
public class LoginController {
      // GulimallWebConfigl类代替了
//    @GetMapping("/login.html")
//    public String loginPage() {
//        return "login";
//    }
//
//    @GetMapping("/reg.html")
//    public String regPage() {
//        return "reg";
//    }

    @Autowired
    private ThirdPartFeignService thirdPartFeignService;

    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        // 1.接口防刷

        // 2.刷新页码也不能60秒内重复发送
        String redisCode     = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) { // 如果redis存在当前手机号的验证码
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000) {
                // 小于60秒不能发送
                return R.error(BizCodeEnum.SMS_EXCEPTION.getCode(), BizCodeEnum.SMS_EXCEPTION.getMsg());
            }
        }

        String code = UUID.randomUUID().toString().substring(0, 5);
        thirdPartFeignService.sendCode(phone, code);

        // 存入redis并设置过期时间 sms:code:13326166085 -> 1234_系统时间
        code = code + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, code, 5, TimeUnit.MINUTES);

        return R.ok();
    }

    /**
     *
     * TODO: 重定向携带数据：利用session原理，将数据放在session中。
     * TODO:只要跳转到下一个页面取出这个数据以后，session里面的数据就会删掉
     * TODO：分布下session问题
     * RedirectAttributes：重定向也可以保留数据，不会丢失
     * 用户注册
     * @return
     */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes attributes) {
        // 如果页码填写有错误
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            attributes.addFlashAttribute("errors",errors);

            //效验出错回到注册页面，因为这是post请求不能直接写return "redict:reg.html"; 因为回到注册页是get请求
            return "redirect:http://auth.gulimall.com/reg.html";
        }


        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        // 验证码通过
        if (!StringUtils.isEmpty(s) && code.equals(s.split("_")[0])) {
            // 删除验证码，防止用户多次使用使用过的验证码
            redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());

            // 调用远程注册服务
            R r = memberFeignService.regist(vo);
            if (r.getCode() == 0) {
                // 注册成功，转到登录界面，要带上域名经过nginx获得静态页面
                return "redirect:http://auth.gulimall.com/login.html";
            } else {
                HashMap<String, String> errors = new HashMap<>();
                errors.put("msg", r.getData(new TypeReference<String>(){}));
                attributes.addFlashAttribute("errors", errors);
                // 注册失败，转发到注册页
                return "redirect:http://auth.gulimall.com/reg.html";
            }

        } else {
            HashMap<String, String> erros = new HashMap<>();
            erros.put("code", "验证码错误");
            attributes.addFlashAttribute("errors", erros);
            // 验证码错误，转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }

    }

}
