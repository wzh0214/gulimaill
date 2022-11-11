package com.wzh.gulimall.ware.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wzh
 * @data 2022/11/8 -14:35
 * 分页配置
 */
@Configuration
@EnableTransactionManagement //开启事务
@MapperScan("com.wzh.gulimall.ware.dao") // 扫描mapper接口所在的包
public class WareMybatisConfig {
    // mybatisPlus 3.4 版本后的才这样配
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
//        溢出总页数后是否进行处理(默认不处理)
        paginationInnerInterceptor.setOverflow(true);
//        单页分页条数限制(默认无限制)
        paginationInnerInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}

