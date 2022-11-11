package com.wzh.gulimall.ware.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/7 -21:01
 */
@NoArgsConstructor
@Data
public class PurchaseDoneVo {
    //{
    //   id: 123,//采购单id
    //   items: [{itemId:1,status:4,reason:""}]//完成/失败的需求详情
    //}

    @NotNull
    private Long id; //采购单id

    private List<PurchaseItemDoneVo> items;

    @NoArgsConstructor
    @Data
    public static class PurchaseItemDoneVo {
        private Long itemId;
        private Integer status;
        private String reason;
    }


}
