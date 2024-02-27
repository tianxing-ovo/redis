package com.ltx.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltx.constant.RedisConstant;
import com.ltx.entity.R;
import com.ltx.entity.ShopType;
import com.ltx.mapper.ShopTypeMapper;
import com.ltx.service.ShopTypeService;
import io.github.tianxingovo.redis.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {

    @Resource
    private RedisUtil redisUtil;


    /**
     * 查询店铺类型
     */
    @Override
    public R queryShopTypeList() {
        String key = RedisConstant.Cache.CACHE_SHOP_TYPE_KEY;
        List<String> shopTypeJsonList = redisUtil.range(key);
        List<ShopType> shopTypeList;
        // 店铺类型不为空
        if (CollUtil.isNotEmpty(shopTypeJsonList)) {
            shopTypeList = shopTypeJsonList.stream().map(shopTypeJson -> JSONUtil.toBean(shopTypeJson, ShopType.class)).collect(Collectors.toList());
            return R.ok(shopTypeList, (long) shopTypeList.size());
        }
        shopTypeList = query().orderByAsc("sort").list();
        shopTypeJsonList = shopTypeList.stream().map(JSONUtil::toJsonStr).collect(Collectors.toList());
        redisUtil.rightPush(key, shopTypeJsonList.toArray(new String[0]));
        redisUtil.expire(key, RedisConstant.Cache.CACHE_SHOP_TYPE_TTL + RandomUtil.randomInt(10), TimeUnit.MINUTES);
        return R.ok(shopTypeList, (long) shopTypeList.size());
    }
}
