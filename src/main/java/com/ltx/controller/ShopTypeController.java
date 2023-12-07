package com.ltx.controller;


import com.ltx.entity.ShopType;
import com.ltx.service.ShopTypeService;
import io.github.tianxingovo.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {

    @Resource
    private ShopTypeService typeService;

    @GetMapping("list")
    public R queryTypeList() {
        List<ShopType> shopTypeList = typeService.query().orderByAsc("sort").list();
        return R.ok().put("shopTypeList", shopTypeList);
    }
}
