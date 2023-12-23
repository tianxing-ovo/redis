package com.ltx.util;

import cn.hutool.core.util.StrUtil;


public class RegexUtil {
    /**
     * 是否是无效手机格式
     */
    public static boolean isPhoneInvalid(String phone) {
        return mismatch(phone, RegexPattern.PHONE_REGEX);
    }

    /**
     * 是否是无效邮箱格式
     */
    public static boolean isEmailInvalid(String email) {
        return mismatch(email, RegexPattern.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     */
    public static boolean isCodeInvalid(String code) {
        return mismatch(code, RegexPattern.VERIFY_CODE_REGEX);
    }

    /**
     * 校验是否不符合正则格式
     */
    private static boolean mismatch(String str, String regex) {
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
