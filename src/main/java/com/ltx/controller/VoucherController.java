package com.ltx.controller;


import com.ltx.entity.Voucher;
import com.ltx.service.VoucherService;
import com.ltx.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Resource
    private VoucherService voucherService;

    /**
     * 新增秒杀券
     */
    @PostMapping("seckill")
    public R addSeckillVoucher(@RequestBody Voucher voucher) {
        voucherService.addSeckillVoucher(voucher);
        return R.ok(voucher.getId());
    }

    /**
     * 新增普通券
     */
    @PostMapping
    public R addVoucher(@RequestBody Voucher voucher) {
        voucherService.save(voucher);
        return R.ok(voucher.getId());
    }


    /**
     * 查询店铺的优惠券列表
     */
    @GetMapping("/list/{shopId}")
    public R queryVoucherOfShop(@PathVariable("shopId") Long shopId) {
        return voucherService.queryVoucherOfShop(shopId);
    }
}
