package com.ltx.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltx.entity.R;
import com.ltx.util.UserHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截需要登录的请求
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    ObjectMapper mapper;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        // 判断是否需要拦截(ThreadLocal中是否有用户)
        if (UserHolder.get() == null) {
            response.setStatus(401);
            try {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write(mapper.writeValueAsString(R.fail("身份验证失败,请登录!")));
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
