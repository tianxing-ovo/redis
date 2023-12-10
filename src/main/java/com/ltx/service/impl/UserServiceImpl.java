package com.ltx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltx.dto.LoginFormDTO;
import com.ltx.dto.UserDTO;
import com.ltx.entity.User;
import com.ltx.mapper.UserMapper;
import com.ltx.service.UserService;
import com.ltx.util.RegexUtils;
import com.ltx.util.UserHolder;
import com.ltx.util.R;
import io.github.tianxingovo.common.SMSUtil;
import io.github.tianxingovo.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ltx.constant.RedisConstant.*;
import static com.ltx.constant.SystemConstant.USER_NICK_NAME_PREFIX;


@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    ObjectMapper mapper;


    /**
     * 发送短信验证码
     */
    @Override
    public R sendCode(String phone) {
        // 校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            return R.fail("手机号格式错误!");
        }
        // 保存验证码到redis中,过期时间为2分钟
        String code = RandomUtil.randomNumbers(6);
        redisUtil.set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 发送验证码
        SMSUtil.sendMessage("tanhua交友", "SMS_276055333", phone, code);
        return R.ok();
    }

    /**
     * 登录
     */
    @Override
    public R login(LoginFormDTO loginForm) {
        // 校验手机号
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            return R.fail("手机号格式错误!");
        }
        // 从redis中获取验证码并校验
        String cacheCode = redisUtil.get(LOGIN_CODE_KEY + phone);
        if (cacheCode == null || !cacheCode.equals(loginForm.getCode())) {
            return R.fail("验证码错误!");
        }
        // 根据手机号查询用户
        User user = query().eq("phone", phone).one();
        // 如果用户不存在,创建新用户
        if (user == null) {
            user = createUserWithPhone(phone);
        }
        // 随机生成token作为key,保存用户信息到redis中
        String token = UUID.randomUUID().toString(true);
        String tokenKey = LOGIN_TOKEN_KEY + token;
        // UserDTO转为Map进行存储
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, String> userMap = mapper.convertValue(userDTO, new TypeReference<Map<String, String>>() {
        });
        redisUtil.putAll(tokenKey, userMap);
        // 设置token有效期,过期时间为30分钟
        redisUtil.expire(tokenKey, LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
        // 返回token给前端
        return R.ok(token);
    }

    @Override
    public R sign() {
        // 获取当前登录用户
        Long userId = UserHolder.getUserId();
        // 获取日期
        LocalDateTime now = LocalDateTime.now();
        // 拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_KEY + userId + keySuffix;
        // 获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();

        return R.ok();
    }

    @Override
    public R signCount() {
        return R.ok();
    }

    /**
     * 根据手机号创建用户
     */
    private User createUserWithPhone(String phone) {
        String nickName = USER_NICK_NAME_PREFIX + RandomUtil.randomString(10);
        // 创建用户
        User user = new User().setPhone(phone).setNickName(nickName);
        // 保存用户
        save(user);
        return user;
    }
}
