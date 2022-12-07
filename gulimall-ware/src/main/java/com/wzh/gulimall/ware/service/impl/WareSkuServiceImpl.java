package com.wzh.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.wzh.common.exception.NoStockException;
import com.wzh.common.to.mq.OrderTo;
import com.wzh.common.to.mq.StockDetailTo;
import com.wzh.common.to.mq.StockLockedTo;
import com.wzh.common.utils.R;
import com.wzh.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.wzh.gulimall.ware.entity.WareOrderTaskEntity;
import com.wzh.gulimall.ware.feign.OrderFeignService;
import com.wzh.gulimall.ware.feign.ProductFeignService;
import com.wzh.gulimall.ware.service.WareOrderTaskDetailService;
import com.wzh.gulimall.ware.service.WareOrderTaskService;
import com.wzh.gulimall.ware.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzh.common.utils.PageUtils;
import com.wzh.common.utils.Query;

import com.wzh.gulimall.ware.dao.WareSkuDao;
import com.wzh.gulimall.ware.entity.WareSkuEntity;
import com.wzh.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;

@RabbitListener(queues = "stock.release.stock.queue")
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private WareOrderTaskService orderTaskService;

    @Autowired
    private WareOrderTaskDetailService orderTaskDetailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    private OrderFeignService orderFeignService;

    /**
     * 1、查询数据库关于这个订单锁定库存信息
     *   没有：无需解锁，因为自己库存锁定失败，自动都回滚了
     *   有：证明库存锁定成功了
     *      解锁：订单状况order表
     *          1、没有这个订单说明下订单服务调的其他服务失败，导致订单回滚但库存服务没回滚，
     *             因为不用分布式事务，所以必须解锁库存
     *          2、有这个订单，不一定解锁库存
     *              订单状态：已取消：解锁库存
     *                      已支付：无需解锁库存
     */
    @Override
    public void unlockStock(StockLockedTo to) {
        //库存工作单的id
        StockDetailTo detail = to.getDetailTo();
        Long detailId = detail.getId();

        WareOrderTaskDetailEntity taskDetailInfo = wareOrderTaskDetailService.getById(detailId);
        if (taskDetailInfo != null) {
            Long id = to.getId();
            WareOrderTaskEntity wareOrderTask = orderTaskService.getById(id);
            // 得到订单号，然后根据订单号查状态
            String orderSn = wareOrderTask.getOrderSn();
            R r = orderFeignService.getOrderStatus(orderSn);
            if (r.getCode() == 0) {
                OrderVo data = r.getData(new TypeReference<OrderVo>() {});
                if (data == null || data.getStatus() == 4) { // 4代表已关闭
                    // 订单不存在或者状态为已关闭，调用unlockStock方法解锁库存
                    if (taskDetailInfo.getLockStatus() == 1) { // 必须是已锁定未解锁状态
                        unLockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                    }
                }
            } else {
               throw new RuntimeException("远程服务失败");
            }
        } else {
            // 无需解锁
        }
    }

    // 防止订单服务卡顿，导致订单状态改不了，库存消息优先到期，查订单状态新建状态，什么都不做就走了
    // 防止卡顿的取消订单，解锁不了库存
    @Transactional
    @Override
    public void unlockStock(OrderTo to) {
        String orderSn = to.getOrderSn();
        //查一下最新状态，防止重复解锁库存
        WareOrderTaskEntity task = orderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = task.getId();
        // 按照工作单找到所有没有解锁的库存，进行解锁
        List<WareOrderTaskDetailEntity> entitys = orderTaskDetailService.list(new QueryWrapper<WareOrderTaskDetailEntity>()
                .eq("task_id", id).eq("lock_status", 1));
        for (WareOrderTaskDetailEntity entity : entitys) {
            unLockStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum(), entity.getId());
        }
    }


    private void unLockStock(Long skuId, Long wareId, Integer skuNum, Long detailId) {
        // 库存解锁
        wareSkuDao.unlockStock(skuId, wareId, skuNum);
        // 更新库存工作单状态
        WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity();
        entity.setId(detailId);
        entity.setLockStatus(2); // 变为已解锁状态
        orderTaskDetailService.updateById(entity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }

        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判读如果没有这个库存记录新增
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(
                new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));

        if (wareSkuEntities == null || wareSkuEntities.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字，如果失败整个事务无需回滚
            //1、自己catch异常
            try{
                R info = productFeignService.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }
            //添加库存信息
            wareSkuDao.insert(wareSkuEntity);
        } else {
            //修改库存信息
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> skuHasStockVos = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            // 查询当前skuId的库存量
            Long count = baseMapper.getSkuStock(skuId);

            vo.setHasStock(count == null ? false :count > 0);
            vo.setSkuId(skuId);
            return vo;
        }).collect(Collectors.toList());

        return skuHasStockVos;

    }

    /**
     * 为订单锁定库存
     * @param vo
     * @return
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        // 保存库存工作单，为了追溯
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        orderTaskService.save(taskEntity);


        List<OrderItemVo> locks = vo.getLocks();

        // 1.找到每个商品在那个仓库有库存
        List<SkuWareHasStockVo> collect = locks.stream().map(item -> {
            SkuWareHasStockVo stock = new SkuWareHasStockVo();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            // 查询这个商品在哪里有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());


        // 2.锁定库存
        for (SkuWareHasStockVo hasStock : collect) {
            boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (wareIds == null || wareIds.size() == 0) {
                // 没有仓库有这个商品
                throw new NoStockException(skuId);
            }

            for (Long wareId : wareIds) {
                // 锁库存，如果成功就返回1，失败返回0
                Long count =  wareSkuDao.lockStock(skuId, wareId, hasStock.getNum());
                if (count == 1) {
                    skuStocked = true;
                    WareOrderTaskDetailEntity entiey = new WareOrderTaskDetailEntity(null, skuId, "", hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    orderTaskDetailService.save(entiey);

                    // TODO 告诉mq锁成功
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(taskEntity.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(entiey,detailTo);
                    lockedTo.setDetailTo(detailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",lockedTo);
                    break;
                } else {
                    //当前仓库锁失败，重试下一个仓库
                }
            }
            if (!skuStocked) {
                // 当前商品所有仓库都没库存
                throw new NoStockException(skuId);
            }

        }

        // 3.全部锁定成功

        return true;
    }



}