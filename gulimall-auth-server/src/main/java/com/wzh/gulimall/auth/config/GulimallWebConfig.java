package com.wzh.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wzh
 * @data 2022/11/19 -20:14
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    /**
     * 视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /**
         *         @GetMapping("/login.html")
         *         public String loginPage() {
         *             return "login";
         *         }
         *
         *         @GetMapping("/reg.html")
         *         public String regPage() {
         *             return "reg";
         *         }
         *
         *         用来代替上面两个跳转的空方法
         */

        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
