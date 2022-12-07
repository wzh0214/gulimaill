package com.wzh.common.to.mq;

import lombok.Data;

import java.util.List;

/**
 * @author wzh
 * @data 2022/12/1 -14:32
 * 发送到mq消息队列的to
 */
@Data
public class StockLockedTo {
    /** 库存工作单的id **/
    private Long id;

    /** 工作单详情**/
    private StockDetailTo detailTo;
}
