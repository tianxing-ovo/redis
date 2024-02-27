package com.ltx.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltx.constant.RedisConstant;
import com.ltx.constant.SystemConstant;
import com.ltx.entity.R;
import com.ltx.entity.RedisData;
import com.ltx.entity.Shop;
import com.ltx.mapper.ShopMapper;
import com.ltx.service.ShopService;
import io.github.tianxingovo.redis.RedisUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {


    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ThreadPoolExecutor executor;

    @Resource
    private ObjectMapper mapper;

    /**
     * 根据商铺id查询商铺详情
     *
     * @param id 商铺id
     */
    @Override
    public R queryById(Long id) {
        return queryByIdWithLogicalExpire(id);
    }


    /**
     * 更新商铺信息
     */
    @Override
    @Transactional
    public R update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return R.fail("店铺id不能为空");
        }
        // 更新数据库
        updateById(shop);
        // 删除缓存
        redisUtil.delete(RedisConstant.Cache.CACHE_SHOP_KEY + id);
        return R.ok();
    }


    @Override
    public R queryShopByType(Integer typeId, Integer current, Double x, Double y) {
        Page<Shop> page = new Page<>(current, SystemConstant.DEFAULT_PAGE_SIZE);
        Page<Shop> shopPage = query().eq("type_id", typeId).page(page);
        return R.ok(shopPage.getRecords(), shopPage.getTotal());
    }

    /**
     * 尝试获取锁
     */
    private boolean tryLock(String key) {
        Boolean b = redisUtil.setIfAbsent(key, UUID.randomUUID().toString(), RedisConstant.Lock.LOCK_SHOP_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(b);
    }

    /**
     * 释放锁
     */
    private void unlock(String key) {
        redisUtil.delete(key);
    }

    /**
     * 互斥锁解决缓存击穿
     */
    @SneakyThrows
    private R queryByIdWithMutex(Long id) {
        String key = RedisConstant.Cache.CACHE_SHOP_KEY + id;
        String str = redisUtil.get(key);
        Shop shop;
        if (StrUtil.isNotBlank(str)) {
            shop = mapper.readValue(str, Shop.class);
            return R.ok(shop);
        }
        if ("".equals(str)) {
            return R.fail("商铺不存在");
        }
        // 使用redis作为互斥锁,防止缓存击穿
        String lockKey = RedisConstant.Lock.LOCK_SHOP_KEY + id;
        try {
            boolean b = tryLock(lockKey);
            if (!b) {
                // 休眠并重试
                Thread.sleep(50);
                return queryById(id);
            }
            shop = getById(id);
            if (shop == null) {
                // 将空字符串写入redis,防止缓存穿透
                redisUtil.set(key, "", RedisConstant.Cache.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return R.fail("商铺不存在");
            }
            // 过期时间加上随机值,防止缓存雪崩
            redisUtil.set(key, mapper.writeValueAsString(shop), RedisConstant.Cache.CACHE_SHOP_TTL + RandomUtil.randomInt(10), TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(lockKey);
        }
        return R.ok(shop);
    }

    /**
     * 逻辑过期解决缓存击穿
     */
    @SneakyThrows
    private R queryByIdWithLogicalExpire(Long id) {
        String key = RedisConstant.Cache.CACHE_SHOP_KEY + id;
        String str = redisUtil.get(key);
        // 热点数据一定存在,为空说明不是热点数据
        if (StrUtil.isBlank(str)) {
            return R.ok();
        }
        RedisData redisData = mapper.readValue(str, RedisData.class);
        // 如果缓存未过期,直接返回数据
        if (LocalDateTime.now().isBefore(redisData.getExpireTime())) {
            return R.ok(redisData.getData());
        }
        // 获取互斥锁
        String lockKey = RedisConstant.Lock.LOCK_SHOP_KEY + id;
        boolean b = tryLock(lockKey);
        // 获取锁成功,新建一个线程重建缓存
        if (b) {
            executor.execute(() -> {
                try {
                    saveShopToRedis(id, 10);
                } finally {
                    unlock(lockKey);
                }
            });
        }
        return R.ok(redisData.getData());
    }

    /**
     * 提前将热点数据加入redis缓存中
     */
    @SneakyThrows
    public void saveShopToRedis(Long id, long seconds) {
        Shop shop = getById(id);
        RedisData redisData = new RedisData(LocalDateTime.now().plusSeconds(seconds), shop);
        redisUtil.set(RedisConstant.Cache.CACHE_SHOP_KEY + id, mapper.writeValueAsString(redisData));
    }
}
