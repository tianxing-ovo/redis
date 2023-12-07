package com.ltx.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltx.constant.SystemConstant;
import com.ltx.entity.Shop;
import com.ltx.mapper.ShopMapper;
import com.ltx.service.ShopService;
import io.github.tianxingovo.common.R;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;




@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;



    @Override
    public R queryById(Long id) {
        // 解决缓存穿透

        // 7.返回
        return R.ok();
    }

    @Override
    @Transactional
    public R update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return R.error(400,"店铺id不能为空");
        }
        // 1.更新数据库
        updateById(shop);
        // 2.删除缓存
        stringRedisTemplate.delete("" + id);
        return R.ok();
    }

    @Override
    public R queryShopByType(Integer typeId, Integer current, Double x, Double y) {
        // 1.判断是否需要根据坐标查询
        if (x == null || y == null) {
            // 不需要坐标查询，按数据库查询
            List<Shop> shopList = query().eq("type_id", typeId)
                    .page(new Page<>(current, SystemConstant.DEFAULT_PAGE_SIZE)).getRecords();
            // 返回数据
            return R.ok().put("shopList",shopList);
        }

        // 2.计算分页参数
        int from = (current - 1) * SystemConstant.DEFAULT_PAGE_SIZE;
        int end = current * SystemConstant.DEFAULT_PAGE_SIZE;

        // 3.查询redis、按照距离排序、分页。结果：shopId、distance
        String key = "" + typeId;
        return R.ok();
    }
}
