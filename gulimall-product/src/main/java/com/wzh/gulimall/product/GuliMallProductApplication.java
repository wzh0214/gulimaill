package com.wzh.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wzh
 * @data 2022/10/25 -19:43
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GuliMallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallProductApplication.class, args);
    }
}
