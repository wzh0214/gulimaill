package com.wzh.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wzh
 * @data 2022/10/25 -20:42
 */

@SpringBootApplication
public class GuliMallCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallCouponApplication.class, args);
    }
}
