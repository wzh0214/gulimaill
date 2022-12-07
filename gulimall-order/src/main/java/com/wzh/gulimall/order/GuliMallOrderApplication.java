package com.wzh.gulimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author wzh
 * @data 2022/10/25 -21:09
 *
 * 使用RabbitMQ
 * 1、引入amqp场景;RabbitAutoConfiguration就会自动生效
 * 2、给容器中自动配置了
 *      RabbitTemplate、AmqpAdmin、CachingConnectionFactory、RabbitMessagingTemplate
 *
 * 3、给配置文件配置spring.rabbitmq信息
 * 4、@EnablRabbit:@EnablRabbitXxxx; 开启功能
 * 5、监听信息：使用@RabbitListener; 必须有@EnableRabbit
 *
 * Seata控制分布式事务
 *  1）、每一个微服务必须创建undo_Log
 *  2）、安装事务协调器：seate-server
 *  3）、整合
 *      1、导入依赖
 *      2、解压并启动seata-server：
 *          registry.conf:注册中心配置    修改 registry ： nacos
 *      3、所有想要用到分布式事务的微服务使用seata DataSourceProxy 代理自己的数据源
 *      4、每个微服务，都必须导入   registry.conf   file.conf
 *          vgroup_mapping.{application.name}-fescar-server-group = "default"
 *      5、启动测试分布式事务
 *      6、给分布式大事务的入口标注@GlobalTransactional
 *      7、每一个远程的小事务用@Trabsactional
 *
 *
 * 本地事务失效的问题：
 *  同一个对象内事务方法互相调用默认失效，因为绕过了代理对象，事务使用代理对象来进行控制的
 * 解决方案：使用代理对象来调用事务方法
 *  1. 引入aop-starter
 *  （1）开启动态代理 @EnableAspectJAutoProxy(exposeProxy=true) (CGLIB) exposeProxy=true 对外暴露代理对象
 *  （2）AopContext.currentProxy(); 获取当前代理对象
 *  2. 直接从 spring 上下文中获取代理对象 (自定义组件需要实现 BeanFactory 接口)
 *   (1) ConfigurableApplicationContext run = SpringApplication.run(GulimallOrderApplication.class, args); 得到上下文
 *   (2) AutowireCapableBeanFactory beanFactory = run.getAutowireCapableBeanFactory(); 得到 bean 工厂
 *   (3) GuLiMallBeanFactory.setBean(beanFactory); 全局化 bean 工厂对象
 *   (4) GuLiMallBeanFactory.getBean(OrderService.class);
 */
@EnableRedisHttpSession
@EnableDiscoveryClient
@EnableFeignClients
@EnableRabbit
@SpringBootApplication
public class GuliMallOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallOrderApplication.class, args);
    }
}
