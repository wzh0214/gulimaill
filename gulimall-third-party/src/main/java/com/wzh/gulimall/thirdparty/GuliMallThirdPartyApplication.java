package com.wzh.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wzh
 * @data 2022/10/31 -17:37
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GuliMallThirdPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallThirdPartyApplication.class, args);
    }
}
