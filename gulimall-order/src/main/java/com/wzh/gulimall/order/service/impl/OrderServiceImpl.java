package com.wzh.gulimall.order.service.impl;

import com.wzh.common.vo.MemberRespVo;
import com.wzh.gulimall.order.feign.CartFeignService;
import com.wzh.gulimall.order.feign.MemberFeignService;
import com.wzh.gulimall.order.interceptor.LoginUserInterceptor;
import com.wzh.gulimall.order.vo.MemberAddressVo;
import com.wzh.gulimall.order.vo.OrderConfirmVo;
import com.wzh.gulimall.order.vo.OrderItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzh.common.utils.PageUtils;
import com.wzh.common.utils.Query;

import com.wzh.gulimall.order.dao.OrderDao;
import com.wzh.gulimall.order.entity.OrderEntity;
import com.wzh.gulimall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private CartFeignService cartFeignService;

    @Autowired
    private ThreadPoolExecutor executor;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();

        // 从threadLocal获得用户
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();

        // 获取当前线程请求头信息(解决Feign异步调用丢失请求头问题)
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //开启第一个异步任务
        CompletableFuture<Void> AddressFuture = CompletableFuture.runAsync(() -> {
            //每一个线程都来共享之前的请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);

            // 1.远程查询根据用户id获得地址信息
            List<MemberAddressVo> address = memberFeignService.getAddressById(memberRespVo.getId());
            if (address != null) {
                orderConfirmVo.setMemberAddressVos(address);
            }
        }, executor);

        //开启第二个异步任务
        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {
            //每一个线程都来共享之前的请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);

            // 2.远程查询购物车选中的购物项
            List<OrderItemVo> items = cartFeignService.getCurrentUserCartItems();
            orderConfirmVo.setItems(items);
        }, executor);


        // 3.查询用户积分
        Integer integration = memberRespVo.getIntegration();
        orderConfirmVo.setIntegration(integration);

        // 4.其他数据自动计算

        // 5.TODO：仿重令牌

        CompletableFuture.allOf(AddressFuture, cartFuture).get();

        return orderConfirmVo;
    }

}