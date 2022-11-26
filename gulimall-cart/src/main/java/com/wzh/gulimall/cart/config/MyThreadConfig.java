package com.wzh.gulimall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wzh
 * @data 2022/11/19 -14:49
 * 自定义线程池
 */
//@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
// 因为ThreadPoolConfigProperties类加了Component，加入组件了，可以直接用，可以不用加上面的注解
@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ThreadPoolExecutor(pool.getCoreSize(), pool.getMaxSize(), pool.getKeepAliveTime(), TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(100000),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy()
                    );

    }


}
