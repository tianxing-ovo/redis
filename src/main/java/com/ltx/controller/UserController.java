package com.ltx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.ltx.dto.LoginFormDTO;
import com.ltx.dto.UserDTO;
import com.ltx.entity.R;
import com.ltx.entity.User;
import com.ltx.entity.UserInfo;
import com.ltx.service.UserInfoService;
import com.ltx.service.UserService;
import com.ltx.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 发送手机验证码
     *
     * @param phone   手机号码
     * @param session 会话
     */
    @PostMapping("/code")
    public R sendCode(@RequestParam("phone") String phone, HttpSession session) {
        return userService.sendCode(phone, session);
    }

    /**
     * 登录
     *
     * @param loginForm 登录请求体
     * @param session   会话
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        return userService.login(loginForm, session);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public R logout() {
        return R.fail("功能未完成");
    }

    /**
     * 获取当前登录的用户并返回
     */
    @GetMapping("/me")
    public R me() {
        return R.ok(UserHolder.get());
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long userId) {
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            return R.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        return R.ok(info);
    }

    @GetMapping("/{id}")
    public R queryUserById(@PathVariable("id") Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return R.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return R.ok(userDTO);
    }

    @PostMapping("/sign")
    public R sign() {
        return userService.sign();
    }

    @GetMapping("/sign/count")
    public R signCount() {
        return userService.signCount();
    }
}