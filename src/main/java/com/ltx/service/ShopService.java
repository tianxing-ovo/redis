package com.ltx.service;

import com.ltx.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.tianxingovo.common.R;


public interface ShopService extends IService<Shop> {

    R queryById(Long id);

    R queryShopByType(Integer typeId, Integer current, Double x, Double y);

    R update(Shop shop);
}