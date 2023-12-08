package com.ltx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.ltx.dto.LoginFormDTO;
import com.ltx.dto.UserDTO;
import com.ltx.entity.User;
import com.ltx.entity.UserInfo;
import com.ltx.service.UserInfoService;
import com.ltx.service.UserService;
import com.ltx.util.UserHolder;
import io.github.tianxingovo.common.R;
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
     */
    @PostMapping("code")
    public R sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // 发送短信验证码并保存验证码
        return userService.sendCode(phone, session);
    }

    /**
     * 登录功能
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        // 实现登录功能
        return userService.login(loginForm, session);
    }

    /**
     * 登出功能
     *
     * @return 无
     */
    @PostMapping("/logout")
    public R logout() {
        return R.error(400, "功能未完成");
    }

    @GetMapping("/me")
    public R me() {
        // 获取当前登录的用户并返回
        UserDTO userDTO = UserHolder.get();
        return R.ok().put("userDTO", userDTO);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long userId) {
        // 查询详情
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return R.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return R.ok().put("info", info);
    }

    @GetMapping("/{id}")
    public R queryUserById(@PathVariable("id") Long userId) {
        // 查询详情
        User user = userService.getById(userId);
        if (user == null) {
            return R.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 返回
        return R.ok().put("userDTO",userDTO);
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