package com.wzh.gulimall.product;

import com.wzh.gulimall.product.entity.BrandEntity;
import com.wzh.gulimall.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wzh
 * @data 2022/10/25 -20:23
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
    @Autowired
    BrandService brandService;

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);

        System.out.println("保存成功");

    }
}
