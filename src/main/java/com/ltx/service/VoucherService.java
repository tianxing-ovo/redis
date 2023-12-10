package com.ltx.service;

import com.ltx.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltx.util.R;


public interface VoucherService extends IService<Voucher> {

    void addSeckillVoucher(Voucher voucher);

    R queryVoucherOfShop(Long shopId);
}