package com.ltx.interceptor;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltx.dto.UserDTO;
import com.ltx.util.UserHolder;
import io.github.tianxingovo.redis.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ltx.constant.RedisConstant.LOGIN_TOKEN_KEY;
import static com.ltx.constant.RedisConstant.LOGIN_TOKEN_TTL;

/**
 * 拦截所有请求
 */
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Resource
    RedisUtil redisUtil;

    @Resource
    ObjectMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        // 获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            return true;
        }
        // 基于token获取redis中的用户
        String key = LOGIN_TOKEN_KEY + token;
        Map<String, String> userMap = redisUtil.entries(key);
        // 如果用户不存在
        if (userMap.isEmpty()) {
            return true;
        }
        // Map转为UserDTO
        UserDTO userDTO = mapper.convertValue(userMap, UserDTO.class);
        // 保存用户信息到ThreadLocal中
        UserHolder.set(userDTO);
        // 只要用户对网页进行访问,就刷新token过期时间
        redisUtil.expire(key, LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        // 移除用户
        UserHolder.remove();
    }
}
