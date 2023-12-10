package com.ltx.service.impl;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltx.entity.ShopType;
import com.ltx.mapper.ShopTypeMapper;
import com.ltx.service.ShopTypeService;
import com.ltx.util.R;
import io.github.tianxingovo.redis.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ltx.constant.RedisConstant.CACHE_SHOP_TYPE_KEY;
import static com.ltx.constant.RedisConstant.CACHE_SHOP_TYPE_TTL;


@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {

    @Resource
    private RedisUtil redisUtil;


    /**
     * 查询店铺类型
     */
    @Override
    public R queryShopTypeList() {
        String key = CACHE_SHOP_TYPE_KEY + "all";
        String shopTypeJson = redisUtil.get(key);
        List<ShopType> shopTypeList;
        if (StrUtil.isNotBlank(shopTypeJson)) {
            shopTypeList = JSONUtil.toList(shopTypeJson, ShopType.class);
            return R.ok(shopTypeList, (long) shopTypeList.size());
        }
        shopTypeList = query().orderByAsc("sort").list();
        redisUtil.set(key, JSONUtil.toJsonStr(shopTypeList), CACHE_SHOP_TYPE_TTL + RandomUtil.randomInt(10), TimeUnit.MINUTES);
        return R.ok(shopTypeList, (long) shopTypeList.size());
    }
}
