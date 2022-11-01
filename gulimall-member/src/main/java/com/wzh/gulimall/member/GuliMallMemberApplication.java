package com.wzh.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wzh
 * @data 2022/10/25 -20:58
 */

@SpringBootApplication
public class GuliMallMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallMemberApplication.class, args);
    }
}
