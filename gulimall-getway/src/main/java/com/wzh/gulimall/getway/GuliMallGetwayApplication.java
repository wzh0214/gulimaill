package com.wzh.gulimall.getway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.sql.DataSource;

/**
 * @author wzh
 * @data 2022/10/29 -15:27
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GuliMallGetwayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallGetwayApplication.class, args);
    }
}
