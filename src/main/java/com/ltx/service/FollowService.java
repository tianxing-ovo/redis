package com.ltx.service;

import com.ltx.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltx.entity.R;


public interface FollowService extends IService<Follow> {

    R follow(Long followUserId, Boolean isFollow);

    R isFollow(Long followUserId);

    R followCommons(Long id);
}