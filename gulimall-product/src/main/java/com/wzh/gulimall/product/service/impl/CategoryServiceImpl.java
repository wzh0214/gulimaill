package com.wzh.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wzh.gulimall.product.service.CategoryBrandRelationService;
import com.wzh.gulimall.product.vo.Catelog2VO;
import lombok.Synchronized;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzh.common.utils.PageUtils;
import com.wzh.common.utils.Query;

import com.wzh.gulimall.product.dao.CategoryDao;
import com.wzh.gulimall.product.entity.CategoryEntity;
import com.wzh.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    private CategoryBrandRelationService CategoryBrandRelationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redisson;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        // 2.组装成父子的树形结构
        List<CategoryEntity> menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map(menu -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return menu1.getSort() - menu2.getSort();
        }).collect(Collectors.toList());

        return menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    //查找完整路径方法
    // [2, 34, 225]
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        return parentPath.toArray(new Long[parentPath.size()]);

    }


    /**
     * 级联更新所有数据
     *
     * @CacheEvict:失效模式
     *  allEntries = true 删除同一分区的数据，即value = "category"的分区
     */
    @CacheEvict(value = "category", allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            CategoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
        // TODO 更新其他关联表冗余字段

    }

    /**
     *  @Cacheable(value = {"category"},key = "'level1Categorys'")
     * 每一个需要缓存的数据指定需要放到那个名字缓存(value); 指定生成缓存的key的名字(key)
     * 代表当前方法需要缓存，如果缓存中有，方法不用调用。如果缓存中没有，会调用方法，最后将方法的结果放入缓存
     */
    @Cacheable(value = "category", key = "'level1Categorys'")
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("调用了getLevel1Categorys()...");
        List<CategoryEntity> entities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return entities;
    }


    @Cacheable(value = "category", key = "#root.method.name")
    @Override
    public Map<String, List<Catelog2VO>> getCatalogJson() {

        // 查出所有
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        // 1.查出所有一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        // 2.封装数据
        Map<String, List<Catelog2VO>> result = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 1.每个一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> entities = getParent_cid(selectList, v.getCatId());
            List<Catelog2VO> catelog2VOS = null;
            // 2.封装上面的结果
            if (entities != null) {
                catelog2VOS = entities.stream().map(l2 -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 1.找当前二级分类的三级分类装成vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2VO.Category3Vo> collect = level3Catelog.stream().map(l3 -> {
                            // 2.封装成指定格式
                            Catelog2VO.Category3Vo category3Vo = new Catelog2VO.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return category3Vo;
                        }).collect(Collectors.toList());
                        catelog2VO.setCatalog3List(collect);
                    }

                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOS;
        }));


        return result;

    }


    public Map<String, List<Catelog2VO>> getCatalogJson2() {
        // 1.加入缓存逻辑，缓存中存的数据是json，因为json跨语言，跨平台兼容
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");

        if (StringUtils.isEmpty(catalogJSON)) {
            System.out.println("缓存不命中，查数据库");
            // 2.缓存中没有，查询数据库，放入redis
            Map<String, List<Catelog2VO>> catalogJsonFromDb = getCatalogJsonFromDbWithRedissonLock();

            return catalogJsonFromDb;
        }

        System.out.println("缓存命中，直接返回");
        // 缓存中有，把json转为对象返回
        Map<String, List<Catelog2VO>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2VO>>>() {
        });
        return result;
    }


    public Map<String, List<Catelog2VO>> getCatalogJsonFromDbWithRedissonLock() {
        // 使用redisson实现分布式锁
        RLock lock = redisson.getLock("CatalogJson-lock");
        lock.lock();

        Map<String, List<Catelog2VO>> dataFromDb;

        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;


    }


    public Map<String, List<Catelog2VO>> getCatalogJsonFromDbWithRedisLock() {
        // 采用分布式锁

        String uuid = UUID.randomUUID().toString();
        // 加锁并设置过期时间，保证两个操作的原子性
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);

        if (lock) {
            System.out.println("获取分布式锁成功...");
            Map<String, List<Catelog2VO>> dataFromDb;

            try {
                dataFromDb = getDataFromDb();
            } finally {
                // 使用lua脚本保证删锁的原子性
                String script = "if redis.call('get', KEYS[1])==ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }

            return dataFromDb;

        } else {
            // 加锁失败，自旋
            System.out.println("获取分布式锁失败重试...");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithRedisLock();
        }


    }

    private Map<String, List<Catelog2VO>> getDataFromDb() {

        // 得到锁后，应该去缓存中确认一次，如果没有在继续，为了防止高并发下其他等待线程抢到锁后去查数据库
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            // 不玩null,直接返回
            Map<String, List<Catelog2VO>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2VO>>>() {
            });
            return result;
        }

        System.out.println("查了数据库");
        // 查出所有
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        // 1.查出所有一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        // 2.封装数据
        Map<String, List<Catelog2VO>> result = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 1.每个一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> entities = getParent_cid(selectList, v.getCatId());
            List<Catelog2VO> catelog2VOS = null;
            // 2.封装上面的结果
            if (entities != null) {
                catelog2VOS = entities.stream().map(l2 -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 1.找当前二级分类的三级分类装成vo
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catelog2VO.Category3Vo> collect = level3Catelog.stream().map(l3 -> {
                            // 2.封装成指定格式
                            Catelog2VO.Category3Vo category3Vo = new Catelog2VO.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return category3Vo;
                        }).collect(Collectors.toList());
                        catelog2VO.setCatalog3List(collect);
                    }

                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOS;
        }));

        // 避免高并发下释放锁后还没放入redis，别的线程又去访问数据库
        String s = JSON.toJSONString(result);
        redisTemplate.opsForValue().set("catalogJSON", s);

        return result;
    }


    public Map<String, List<Catelog2VO>> getCatalogJsonFromDb() {
        //只要是同一把锁，就能锁住需要这个锁的线程
        // synchronized (this)：因为SpringBoot的所有组件在容器中是单实例，所以可以用this
        // TODO 如果是分布式的话，有多个商品服务，想要锁住所有必须用分布式锁
        synchronized (this) {
            // 得到锁后，应该去缓存中确认一次，如果没有在继续，为了防止高并发下其他等待线程抢到锁后去查数据库
            return getDataFromDb();
        }


    }


    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item ->
                item.getParentCid().equals(parent_cid)
        ).collect(Collectors.toList());
        return collect;
    }

    //递归查找父节点id
    public List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        paths.add(catelogId);
        return paths;
    }


    // 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == root.getCatId()
        ).map(categoryEntity -> {
            // 1.找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            // 2.菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;

    }

}