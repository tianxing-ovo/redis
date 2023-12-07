package com.ltx.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltx.constant.SystemConstant;
import com.ltx.entity.Shop;
import com.ltx.service.ShopService;
import io.github.tianxingovo.common.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    public ShopService shopService;

    /**
     * 根据id查询商铺信息
     */
    @GetMapping("/{id}")
    public R queryShopById(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }

    /**
     * 新增商铺信息
     *
     * @param shop 商铺数据
     * @return 商铺id
     */
    @PostMapping
    public R saveShop(@RequestBody Shop shop) {
        // 写入数据库
        shopService.save(shop);
        // 返回店铺id
        return R.ok().put("id", shop.getId());
    }

    /**
     * 更新商铺信息
     */
    @PutMapping
    public R updateShop(@RequestBody Shop shop) {
        shopService.update(shop, null);
        return R.ok();
    }

    /**
     * 根据商铺类型分页查询商铺信息
     */
    @GetMapping("/of/type")
    public R queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "x", required = false) Double x,
            @RequestParam(value = "y", required = false) Double y
    ) {
        return shopService.queryShopByType(typeId, current, x, y);
    }

    /**
     * 根据商铺名称关键字分页查询商铺信息
     */
    @GetMapping("/of/name")
    public R queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        List<Shop> shopList = shopService.query()
                .like(StrUtil.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstant.MAX_PAGE_SIZE)).getRecords();

        return R.ok().put("shopList", shopList);
    }
}
