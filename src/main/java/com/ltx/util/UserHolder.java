package com.ltx.util;

import com.ltx.dto.UserDTO;

/**
 * 保存/获取用户信息
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void set(UserDTO userDTO) {
        threadLocal.set(userDTO);
    }

    public static UserDTO get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static Long getUserId() {
        return get().getId();
    }
}
