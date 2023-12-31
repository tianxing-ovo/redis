package com.ltx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltx.entity.Shop;
import com.ltx.entity.R;


public interface ShopService extends IService<Shop> {

    R queryById(Long id);

    R queryShopByType(Integer typeId, Integer current, Double x, Double y);

    R update(Shop shop);
}