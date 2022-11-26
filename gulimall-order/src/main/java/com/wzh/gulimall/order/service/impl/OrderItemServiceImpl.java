package com.wzh.gulimall.order.service.impl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wzh.gulimall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzh.common.utils.PageUtils;
import com.wzh.common.utils.Query;

import com.wzh.gulimall.order.dao.OrderItemDao;
import com.wzh.gulimall.order.entity.OrderItemEntity;
import com.wzh.gulimall.order.service.OrderItemService;

@RabbitListener(queues = {"heollo-java-queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * queues：声明需要监听的队列
     * channel：当前传输数据的通道
     */
//    @RabbitListener(queues = {"heollo-java-queue"})
    @RabbitHandler
    public void revieveMessage(Message message,
                               OrderReturnReasonEntity content, Channel channel) throws IOException {
        //拿到主体内容
        byte[] body = message.getBody();
        //拿到的消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();

        System.out.println("接受到的消息...内容" + message + "===内容：" + content);

        // channrl 内按顺序自增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 确认签收，false非批量模式
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {

        }

    }

}