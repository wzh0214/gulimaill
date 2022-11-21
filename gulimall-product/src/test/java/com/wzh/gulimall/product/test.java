package com.wzh.gulimall.product;

import com.aliyun.oss.OSSClient;
import com.wzh.gulimall.product.dao.AttrGroupDao;
import com.wzh.gulimall.product.dao.SkuSaleAttrValueDao;
import com.wzh.gulimall.product.entity.BrandEntity;
import com.wzh.gulimall.product.service.BrandService;
import com.wzh.gulimall.product.service.CategoryService;
import com.wzh.gulimall.product.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    OSSClient ossClient;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redisson;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);

        System.out.println("保存成功");

    }

    @Test
    public void testUpload() throws FileNotFoundException {

        //上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\14368\\Pictures\\Saved Pictures\\1.jpg");
        ossClient.putObject("gulimall-178", "penguin4.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功.");
    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(255L);
        log.info("路径：" + Arrays.asList(catelogPath));
    }


    @Test
    public void testStringRedisTemplate() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("k1", "v1");
        System.out.println(ops.get("k1"));
    }

    @Test
    public void testRedisson() {
        System.out.println(redisson);
    }

    @Test
    public void test() {
        List<SkuItemVo.SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId =
                attrGroupDao.getAttrGroupWithAttrsBySpuId(1L, 225L);

        System.out.println(attrGroupWithAttrsBySpuId);


    }

    @Test
    public void test2() {
        List<SkuItemVo.SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(1L);
        System.out.println(saleAttrsBySpuId);


    }

}
