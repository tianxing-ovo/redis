package com.ltx.service;

import com.ltx.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltx.util.R;


public interface VoucherOrderService extends IService<VoucherOrder> {

    R seckillVoucher(Long voucherId);
}