package com.wzh.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import com.wzh.gulimall.order.config.AlipayTemplate;
import com.wzh.gulimall.order.service.OrderService;
import com.wzh.gulimall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wzh
 * @data 2022/12/3 -17:29
 */
@Controller
public class PayWebController {
    @Autowired
    private AlipayTemplate alipayTemplate;

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单:支付宝支付
     * 1、让支付页让浏览器展示
     * 2、支付成功以后，跳转到用户的订单列表页
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    @ResponseBody
    @GetMapping(value = "/aliPayOrder",produces = "text/html") // 响应的是html不是json
    public String aliPayOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {

        PayVo payVo = orderService.getOrderPay(orderSn);
        // 返回的是一个页面，将此页面直接交给浏览器就行
        String pay = alipayTemplate.pay(payVo);
        return pay;
    }
}
