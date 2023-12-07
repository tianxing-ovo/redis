package com.ltx.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltx.entity.UserInfo;
import com.ltx.mapper.UserInfoMapper;
import com.ltx.service.UserInfoService;
import org.springframework.stereotype.Service;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
