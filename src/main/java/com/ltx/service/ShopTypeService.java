package com.ltx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltx.entity.ShopType;
import com.ltx.util.R;


public interface ShopTypeService extends IService<ShopType> {

    R queryShopTypeList();

}