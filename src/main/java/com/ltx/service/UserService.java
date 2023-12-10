package com.ltx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltx.dto.LoginFormDTO;
import com.ltx.entity.User;
import com.ltx.entity.R;


public interface UserService extends IService<User> {

    R signCount();

    R login(LoginFormDTO loginForm);

    R sendCode(String phone);

    R sign();
}