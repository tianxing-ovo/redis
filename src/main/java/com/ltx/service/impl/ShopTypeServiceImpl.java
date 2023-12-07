package com.ltx.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltx.entity.ShopType;
import com.ltx.mapper.ShopTypeMapper;
import com.ltx.service.ShopTypeService;
import org.springframework.stereotype.Service;


@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {

}
