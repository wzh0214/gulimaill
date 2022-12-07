package com.wzh.gulimall.ware.listener;

import com.rabbitmq.client.Channel;
import com.wzh.common.to.mq.OrderTo;
import com.wzh.common.to.mq.StockLockedTo;
import com.wzh.gulimall.ware.service.WareSkuService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author wzh
 * @data 2022/12/1 -19:02
 */
@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {
    @Autowired
    private WareSkuService wareSkuService;

    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        System.out.println("手动解锁库存的消息");
        try {
            wareSkuService.unlockStock(to);
            // 接收成功，手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 如果接受消息失败，重新归队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    @RabbitHandler
    public void handleOrderCloseRelease(OrderTo to, Message message, Channel channel) throws IOException {
        System.out.println("订单关闭准备解锁库存");
        try {
            wareSkuService.unlockStock(to);
            // 接收成功，手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 如果接受消息失败，重新归队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }


}
