package com.wzh.guliamll.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wzh
 * @data 2022/11/9 -19:17
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GuliMallSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallSearchApplication.class, args);
    }
}