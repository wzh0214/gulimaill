package com.wzh.gulimall.order;

import com.wzh.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

/**
 * @author wzh
 * @data 2022/11/25 -16:37
 */
@Slf4j
@SpringBootTest
public class GulimallOrderApplicationTest {
    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessageTest() {
        String msg = "hello,rabbitmq";
        rabbitTemplate.convertAndSend("heollo-java-exchange", "hello.java", msg);
        log.info("消息发送成功");
    }

    @Test
    public void sendObjectMessageTest() {
        //1、发送消息,如果发送的消息是个对象，会使用序列化机制，将对象写出去，对象必须实现Serializable接口
        OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
        reasonEntity.setId(1L);
        reasonEntity.setCreateTime(new Date());
        reasonEntity.setName("reason");
        reasonEntity.setStatus(1);
        reasonEntity.setSort(2);
        //2、发送的对象类型的消息，可以是一个json，配置了MessageConverter
        // new CorrelationData(UUID.randomUUID().toString())可以给这个消息设个唯一id，好确认消息是否可靠送达
        rabbitTemplate.convertAndSend("heollo-java-exchange", "hello.java", reasonEntity,
                new CorrelationData(UUID.randomUUID().toString()));
        log.info("消息发送成功");
    }


    @Test
    public void createExchange() {
        DirectExchange directExchange = new DirectExchange("heollo-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功", "heollo-java-exchange");

    }

    @Test
    public void createQueue() {
        Queue queue = new Queue("heollo-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功", "heollo-java-queue");

    }

    @Test
    public void createBinding() {
        Binding binding = new Binding("heollo-java-queue", Binding.DestinationType.QUEUE,
                "heollo-java-exchange", "hello.java", null);
        amqpAdmin.declareBinding(binding);
        log.info("Binding成功");

    }
}
