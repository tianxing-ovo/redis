package com.ltx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltx.entity.ShopType;
import com.ltx.entity.R;


public interface ShopTypeService extends IService<ShopType> {

    R queryShopTypeList();

}