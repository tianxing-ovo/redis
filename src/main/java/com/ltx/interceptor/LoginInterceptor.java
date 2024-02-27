package com.ltx.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltx.constant.SystemConstant;
import com.ltx.dto.UserDTO;
import com.ltx.entity.R;
import com.ltx.util.UserHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 拦截需要登录的请求
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private ObjectMapper mapper;

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

    /**
     * 基于session进行登录拦截
     */
    private boolean sessionInterceptor(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Object user = session.getAttribute(SystemConstant.SESSION_ATTRIBUTE_USER);
        // 用户不存在,进行拦截
        if (Objects.isNull(user)) {
            response.setStatus(401);
            return false;
        }
        // 保存用户信息到ThreadLocal中
        UserHolder.set((UserDTO) user);
        return true;
    }
}
