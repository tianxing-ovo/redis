package com.ltx.service;

import com.ltx.dto.LoginFormDTO;
import com.ltx.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.tianxingovo.common.R;

import javax.servlet.http.HttpSession;


public interface UserService extends IService<User> {

    R signCount();

    R login(LoginFormDTO loginForm, HttpSession session);

    R sendCode(String phone, HttpSession session);

    R sign();
}