package com.wzh.gulimall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wzh
 * @data 2022/11/19 -18:51
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GuliMallAuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallAuthServerApplication.class, args);
    }
}
