package com.wzh.gulimall.product.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author wzh
 * @data 2022/11/15 -13:29
 */
@Configuration
public class MyRedissonConfig {
    /**
     * 所有对Redisson的使用都是通过RedissonClient对象
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        // 1.创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.163.129:6379");

        // 2.根据Config创建RedissonClient示例
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
