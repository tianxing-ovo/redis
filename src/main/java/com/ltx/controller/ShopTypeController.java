package com.ltx.controller;


import com.ltx.service.ShopTypeService;
import com.ltx.entity.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {

    @Resource
    private ShopTypeService typeService;

    /**
     * 查询店铺类型
     */
    @GetMapping("list")
    public R queryTypeList() {
        return typeService.queryShopTypeList();
    }
}
